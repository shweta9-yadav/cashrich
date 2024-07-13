package com.example.cashrichdata.message;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MessageStatus<T> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int StatusCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    private String response;

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public MessageStatus(String message, int statusCode) {
        super();
        this.StatusCode = statusCode;
        this.message = message;
    }

    public MessageStatus(String message, int statusCode,String response) {
        super();
        this.StatusCode = statusCode;
        this.message = message;
        if(response!=null) {
            this.response = response;
        }
    }
}
