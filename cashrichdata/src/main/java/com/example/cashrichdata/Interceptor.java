package com.example.cashrichdata;

import com.example.cashrichdata.dto.UserAuth;
import com.example.cashrichdata.repo.UserAuthRepo;
import com.example.cashrichdata.serviceimpl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.util.Date;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;


@Transactional
@Component
public class Interceptor implements HandlerInterceptor {

    @Autowired
    private UserAuthRepo userAuthRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    private final Logger logger = LoggerFactory.getLogger(Interceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestUrl = request.getRequestURI();
        String methodType = request.getMethod();

        if (methodType.equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        if (requestUrl.contains("/login") || requestUrl.contains("/upsert") ) {
            return true;
        }

        String token = request.getHeader("Authorization");
        System.out.println("token"+token);

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.info("Request without authentication token not allowed");
            return false;
        }


        UserAuth userAuth = userAuthRepository.findByToken(token);

        if (userAuth == null || userAuth.getExpiredTime().before(new Date())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.info("Invalid or expired authentication token");
            return false;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredLocalDateTime = userAuth.getExpiredTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long hoursPassed = ChronoUnit.HOURS.between(expiredLocalDateTime, currentTime);

        if (hoursPassed >= 12) {
            Date expiredTime = userServiceImpl.getExpiredTime(0, 24);
            userAuthRepository.updateExpiredDateByTokenId(token, expiredTime);
        }

        // Check API access permissions
        String apiEndpoint = requestUrl.replace("/api", "").split("\\?")[0];
        logger.info("Request URL to check access: " + apiEndpoint);
        System.out.println(isApiAccess(apiEndpoint)+"---"+idPresentInRequestBody(request,requestUrl));
        if (isApiAccess(apiEndpoint) && !idPresentInRequestBody(request,requestUrl)) {
            request.setAttribute("userAuth", userAuth);
            response.setStatus(HttpServletResponse.SC_OK);
            logger.info("User authenticated for API access");
            return true;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        logger.info("Unauthorized API access");
        return false;
    }

    private boolean isApiAccess(String requestUrl) {
        List<String> allowedApis = Arrays.asList(("/user/upsert,/user/access").split(","));
        return allowedApis.stream().anyMatch(api -> api.equals(requestUrl));
    }

    private boolean idPresentInRequestBody(HttpServletRequest request,String requestUrl) {
        if(requestUrl.contains("/user/upsert")) {
            String requestBody = getRequestBody(request);
            if (requestBody != null && requestBody.contains("\"id\"")) {
                return true;
            }
            return false;
        }
        return false;
    }

    private String getRequestBody(HttpServletRequest request) {
        StringBuilder requestBodyBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBodyBuilder.append(line);
            }
        } catch (IOException e) {
            logger.error("Error reading request body", e);
        }
        return requestBodyBuilder.toString();
    }

}
