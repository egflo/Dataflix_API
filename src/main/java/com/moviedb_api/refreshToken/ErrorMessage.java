package com.moviedb_api.refreshToken;

import java.util.Date;

public class ErrorMessage {
    private Integer status;
    private String message;
    private String description;
    private Date timestamp;

    public ErrorMessage(Integer status, Date timestamp, String message, String description) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
