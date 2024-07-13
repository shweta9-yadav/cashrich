package com.example.cashrichdata.serviceimpl;

import com.example.cashrichdata.dto.User;
import com.example.cashrichdata.dto.UserAuth;
import com.example.cashrichdata.dto.thirdpartyResponse;
import com.example.cashrichdata.generation.EncryptionDecryption;
import com.example.cashrichdata.generation.GenericUtility;
import com.example.cashrichdata.generation.ResourceNotFound;
import com.example.cashrichdata.message.MessageStatus;
import com.example.cashrichdata.model.UserAuthInput;
import com.example.cashrichdata.model.UserAuthOutput;
import com.example.cashrichdata.model.UserModel;
import com.example.cashrichdata.repo.UserAuthRepo;
import com.example.cashrichdata.repo.UserRepo;
import com.example.cashrichdata.repo.thirdpartyresponserep;
import com.example.cashrichdata.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserAuthRepo userAuthRepo;

    @Autowired
    thirdpartyresponserep thirdpartyresponsereps;


    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);



    @Override
    public MessageStatus upsertUser(UserAuth userAuth, UserModel userDto) {

        MessageStatus msg = new MessageStatus();
        try {

            Long userId=userDto.getId();
            if(!GenericUtility.isValidUserName(userDto.getUserName())){
                return new MessageStatus("User Name is invalid.", HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
            if(!GenericUtility.isValidPassword(userDto.getPassword())){
                return new MessageStatus("Password is invalid.", HttpServletResponse.SC_NOT_ACCEPTABLE);
            }

            if ( userId == null) { // create user
                try {
                    String emaildata = userRepo.findByEmail(userDto.getEmail());
                    String usernameduplicate = userRepo.findByUserName(userDto.getUserName());
                    if(emaildata!=null){
                        return new MessageStatus(userDto.getEmail() +" already exists!",
                                HttpServletResponse.SC_NOT_ACCEPTABLE);
                    }
                    if(usernameduplicate!=null){
                        return new MessageStatus(userDto.getUserName() +" already exists!",
                                HttpServletResponse.SC_NOT_ACCEPTABLE);
                    }
                    User user= new User();
                    logger.info("Inside create User");
                    BeanUtils.copyProperties(userDto,user,GenericUtility.getNullPropertyNames(userDto));
                    user.setPassword(EncryptionDecryption.encryptAndEncode(userDto.getPassword()));

                    try {
                        userRepo.save(user);
                    }
                    catch (DataIntegrityViolationException e) {
                        throw new DataIntegrityViolationException("User EmailId "+userDto.getEmail() + " already exists. Kindly use another Email Id");
                    }
                    logger.info("User is created successfully.");
                    return new MessageStatus("User is created successfully.", HttpServletResponse.SC_CREATED);
                } catch (Exception e) {
                    logger.error("Exception Occured while creating User",e);
                }
            } 	else {

                try {
                    logger.info("Inside Update User");

                    User userDb=null;
                    userDb = userRepo.findById(userDto.getId()).orElse(null);
                    if(userDb==null){
                        return new MessageStatus("User is not found.", HttpServletResponse.SC_NOT_ACCEPTABLE);

                    }
                    userDto.setEmail(userDb.getEmail());
                    userDto.setUserName(userDb.getUserName());
                    BeanUtils.copyProperties(userDto,userDb,GenericUtility.getNullPropertyNames(userDto));

                    User updateUser = userRepo.save(userDb);
                    msg.setStatusCode(HttpServletResponse.SC_OK);

                    if (updateUser == null) throw new ResourceNotFound("User not found");

                    else {
                        logger.info("User is updated successfully.");
                        return new MessageStatus("User is updated successfully.", HttpServletResponse.SC_OK);
                    }
                }catch (Exception e) {
                    logger.error("Exception occured while Updating User",e);
                }
            }
            return msg;
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("Exception occured inside User service upsertUser implementation", e);
            e.printStackTrace();
        }
        return msg;
    }
    @Override
    public MessageStatus auth(UserAuthInput userAuthInput, HttpServletRequest req) throws ResourceNotFound{
        MessageStatus msg = new MessageStatus();
        try {
            String encPassword="" ;
            User user=null;
            try {
                encPassword = EncryptionDecryption.encryptAndEncode(userAuthInput.getPassword());
                System.out.println("password ==========="+encPassword);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             user = userRepo.findByUserNameAndPassword(userAuthInput.getUserName(), encPassword);// need to encrypt password
            if (user == null) {
                    return new MessageStatus("Invalid details. Please check the userName or password combination.", HttpServletResponse.SC_OK);
            }

            UserAuth userAuth = new UserAuth();
            userAuth.setUserName(user.getUserName());
            userAuth.setEmail(user.getEmail());
            userAuth.setFirstName(user.getFirstName());
            userAuth.setLastName(user.getLastName());

            userAuth.setLoginTime(new Date());
            userAuth.setExpiredTime(getExpiredTime(0,24));



            userAuthRepo.save(userAuth);

            UserAuthOutput userAuthOutput = new UserAuthOutput();
            userAuthOutput.setFirstName(user.getFirstName());
            userAuthOutput.setLastName(user.getLastName());
            userAuthOutput.setEmail(user.getEmail());
            userAuthOutput.setTokenExpiredTime(userAuth.getExpiredTime());

            userAuthOutput.setTokenId(userAuth.getToken());



            msg.setMessage("Login successful.");
            msg.setStatusCode(HttpServletResponse.SC_OK);
            msg.setResponse(userAuth.getToken());
            return msg;
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("Exception occured inside User service auth implementation", e);
            e.printStackTrace();
        }
        return msg;
    }


    @Override
    public MessageStatus access(Long userId, String search, HttpServletRequest req) {
        MessageStatus msg = new MessageStatus();
        String responseBody;

        try {
            responseBody = getSearchResponseData(search);
        } catch (Exception e) {
            msg.setStatusCode(500);
            msg.setMessage("Failed to retrieve data: " + e.getMessage());
            return msg;
        }

        if (userId != null) {
            try {
                thirdpartyResponse resp = new thirdpartyResponse();
                resp.setUserId(userId);
                resp.setResponse(responseBody);
                resp.setTimestamp(LocalDateTime.now());
                thirdpartyresponsereps.save(resp);
            } catch (Exception e) {
                msg.setStatusCode(500);
                msg.setMessage("Failed to save data: " + e.getMessage());
                return msg;
            }
        }

        msg.setStatusCode(200);
        msg.setMessage("Success");
        msg.setResponse(responseBody);
        return msg;
    }

    private String getSearchResponseData(String search) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", "27ab17d1-215f-49e5-9ca4-afd48810c149");

        // Add the search query parameter properly
        String searchQuery = url + "?symbol=" + search;

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(searchQuery, HttpMethod.GET, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Failed to retrieve data from third party: " + response.getStatusCode());
        }

        return response.getBody();
    }
    public Date getExpiredTime(int minute,int hour) {
        Calendar now = Calendar.getInstance();
        if(minute >0) now.add(Calendar.MINUTE, minute);
        if(hour >0) now.add(Calendar.HOUR, hour);
        return now.getTime();
    }
}
