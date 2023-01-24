package com.example.topgoback.Users.DTO;

public class InviteFriendDTO {
    private int userId;
    private int invitedId;
    private String status;

    public InviteFriendDTO() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getInvitedId() {
        return invitedId;
    }

    public void setInvitedId(int invitedId) {
        this.invitedId = invitedId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
