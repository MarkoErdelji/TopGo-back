package com.example.topgoback.Users.DTO;

import com.example.topgoback.Users.Model.User;

public class UserRef {
    Integer id;

    String email;

    public UserRef() {
    }

    public UserRef(User user) {
      this.id = user.getId();
      this.email = user.getEmail();
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
