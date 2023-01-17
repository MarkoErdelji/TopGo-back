package com.example.topgoback.RejectionLetters.DTO;

public class RejectionTextDTO {

    private String reason;

    public RejectionTextDTO(String s) {
        this.reason = s;
    }

    public RejectionTextDTO() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
