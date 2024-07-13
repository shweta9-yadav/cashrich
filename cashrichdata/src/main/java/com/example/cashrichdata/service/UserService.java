package com.example.cashrichdata.service;

import com.example.cashrichdata.dto.UserAuth;
import com.example.cashrichdata.generation.ResourceNotFound;
import com.example.cashrichdata.message.MessageStatus;
import com.example.cashrichdata.model.UserAuthInput;
import com.example.cashrichdata.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    MessageStatus upsertUser(UserAuth userAuth, UserModel user);
    public MessageStatus auth(UserAuthInput userAuthInput, HttpServletRequest req) throws ResourceNotFound;

    MessageStatus access(Long userId,String search,HttpServletRequest req);
}
