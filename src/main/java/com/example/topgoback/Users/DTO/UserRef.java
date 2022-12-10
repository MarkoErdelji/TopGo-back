package com.example.topgoback.Users.DTO;

public class UserRef {
    Integer id;

    String email;

    public UserRef() {
    }

    public static UserRef getMockupData(){
        UserRef userRef = new UserRef();
        userRef.setEmail("user@example.com");
        userRef.setId(123);

        return userRef;
    }
    public UserRef(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
