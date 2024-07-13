package com.example.cashrichdata.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UserModel {

    private Long id;

    @NotEmpty(message = "FirstName is mandatory")
    private String firstName;
    private String lastName;
    @NotEmpty(message = "Email should not be Empty")
    @Email(message = "Please enter a Valid EmailId")
    private String email;
    @NotEmpty
    private String password;

    private Integer mobile;

    @NotEmpty(message = "User Name is mandatory")
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName) {
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

    public void setEmail( String email) {
        this.email = email;
    }

    public @NotEmpty String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public  String getUserName() {
        return userName;
    }

    public void setUserName( String userName) {
        this.userName = userName;
    }
}
