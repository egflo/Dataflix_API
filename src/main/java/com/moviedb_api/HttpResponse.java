package com.moviedb_api;

public class HttpResponse {
    public int status;
    public Boolean success;
    public String message;
    public Object data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
