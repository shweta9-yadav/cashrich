package com.example.cashrichdata.dto;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="respond")
public class thirdpartyResponse {
    @Id
    private Long userId;

    private String response;
    private LocalDateTime timestamp;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
