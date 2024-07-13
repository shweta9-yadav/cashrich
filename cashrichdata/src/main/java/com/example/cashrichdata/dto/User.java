package com.example.cashrichdata.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "FirstName is mandatory")
    @NotEmpty(message = "FirstName is mandatory")
    private String firstName;

    @NotNull(message = "LastName is mandatory")
    private String lastName;

    @NotNull(message = "Email is mandatory")
    @NotEmpty(message = "Email is mandatory")
    private String email;

    private Integer mobile;

    @NotNull(message = "password is mandatory")
    @NotEmpty(message = "password is mandatory")
    private String password;

    @NotNull(message = "User Name is mandatory")
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

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
