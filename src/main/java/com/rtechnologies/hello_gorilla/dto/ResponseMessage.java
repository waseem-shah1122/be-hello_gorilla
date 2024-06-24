package com.rtechnologies.hello_gorilla.dto;

public class ResponseMessage {

    private boolean status;
    private String message;

    public ResponseMessage() {}

    public ResponseMessage(boolean status, String message) {
        this.status = status;
        this.message = message;

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
