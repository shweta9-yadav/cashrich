package com.example.cashrichdata.controller;

import com.example.cashrichdata.dto.UserAuth;
import com.example.cashrichdata.generation.ResourceNotFound;
import com.example.cashrichdata.message.MessageStatus;
import com.example.cashrichdata.model.UserAuthInput;
import com.example.cashrichdata.model.UserModel;
import com.example.cashrichdata.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="/api/user")
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping(value = { "/upsert" })
    public ResponseEntity<MessageStatus> upsertUser(@Valid @RequestBody UserModel user,
                                                    HttpServletRequest req, HttpServletResponse resp) {

        MessageStatus msg = new MessageStatus();
        UserAuth userAuth = (UserAuth) req.getAttribute("userAuth");
        msg = userService.upsertUser(userAuth, user);
        if(msg.getStatusCode()==HttpServletResponse.SC_OK||msg.getStatusCode()==HttpServletResponse.SC_CREATED)
            return ResponseEntity.ok().body(msg);
        else
            return ResponseEntity.badRequest().body(msg);
    }
    @PostMapping(value = "/login")
    public ResponseEntity<MessageStatus> login(@Valid @RequestBody UserAuthInput login, HttpServletRequest req,
                                               HttpServletResponse resp) throws ResourceNotFound {
        MessageStatus msg= userService.auth(login, req);
        if(msg.getStatusCode()==HttpServletResponse.SC_OK||msg.getStatusCode()==HttpServletResponse.SC_CREATED)
            return ResponseEntity.ok().body(msg);
        else
            return ResponseEntity.badRequest().body(msg);
    }

    @GetMapping(value = "/access")
    public ResponseEntity<MessageStatus> login(@RequestParam("userId") Long userId,@RequestParam("search") String search, HttpServletRequest req,
                                               HttpServletResponse resp) throws ResourceNotFound {
        MessageStatus msg= userService.access(userId,search,req);
        if(msg.getStatusCode()==HttpServletResponse.SC_OK||msg.getStatusCode()==HttpServletResponse.SC_CREATED)
            return ResponseEntity.ok().body(msg);
        else
            return ResponseEntity.badRequest().body(msg);
    }
}
