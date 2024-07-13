package com.example.cashrichdata.model;

import java.util.Date;

public class UserAuthOutput {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private Date tokenExpiredTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setTokenId(String token) {
        this.token = token;
    }

    public Date getTokenExpiredTime() {
        return tokenExpiredTime;
    }

    public void setTokenExpiredTime(Date tokenExpiredTime) {
        this.tokenExpiredTime = tokenExpiredTime;
    }
}
