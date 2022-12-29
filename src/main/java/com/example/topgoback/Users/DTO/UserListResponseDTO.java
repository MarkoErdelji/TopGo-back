package com.example.topgoback.Users.DTO;

import com.example.topgoback.Users.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserListResponseDTO {
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public UserListResponseDTO() {
    }

    public UserListResponseDTO(User u) {
        id = u.getId();
        name = u.getFirstName();
        surname = u.getLastName();
        profilePicture = u.getProfilePicture();
        telephoneNumber = u.getPhoneNumber();
        email = u.getEmail();
        address = u.getAddress();
    }

    public static UserListResponseDTO getMockupData(){
        UserListResponseDTO userListResponseDTO = new UserListResponseDTO();

        userListResponseDTO.setId(10);
        userListResponseDTO.setName("Pera");
        userListResponseDTO.setSurname("Peric");
        userListResponseDTO.setProfilePicture("U3dhZ2dlciByb2Nrcw==");
        userListResponseDTO.setTelephoneNumber("+381123123");
        userListResponseDTO.setEmail("pera.peric@email.com");
        userListResponseDTO.setAddress("Bulevar Oslobodjenja 74");

        return userListResponseDTO;

    }

    public static List<UserListResponseDTO> convertToUserListResponseDTO(List<User> users){
        List<UserListResponseDTO> userListResponseDTOS = new ArrayList<UserListResponseDTO>();
        for(User u:users){
            userListResponseDTOS.add(new UserListResponseDTO(u));
        }
        return userListResponseDTOS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
