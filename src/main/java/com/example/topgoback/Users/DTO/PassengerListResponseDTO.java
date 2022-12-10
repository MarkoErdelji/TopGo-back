package com.example.topgoback.Users.DTO;

import com.example.topgoback.Tools.PaginatedResponse;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;

import java.util.ArrayList;
import java.util.List;

public class PassengerListResponseDTO{
    private Integer id;
    private String name;
    private String surname;
    private String profilePicture;
    private String telephoneNumber;
    private String email;
    private String address;

    public PassengerListResponseDTO(){
    }

    public PassengerListResponseDTO(Passenger p) {
        id = p.getId();
        name = p.getFirstName();
        surname = p.getLastName();
        profilePicture = p.getProfilePicture();
        telephoneNumber = p.getPhoneNumber();
        email = p.getEmail();
        address = p.getAddress();
    }

    public static PassengerListResponseDTO getMockupData(){
        PassengerListResponseDTO passengerListResponseDTO = new PassengerListResponseDTO();

        passengerListResponseDTO.setId(10);
        passengerListResponseDTO.setName("Pera");
        passengerListResponseDTO.setSurname("Peric");
        passengerListResponseDTO.setProfilePicture("U3dhZ2dlciByb2Nrcw==");
        passengerListResponseDTO.setTelephoneNumber("+381123123");
        passengerListResponseDTO.setEmail("pera.peric@email.com");
        passengerListResponseDTO.setAddress("Bulevar Oslobodjenja 74");

        return passengerListResponseDTO;

    }

    public static List<PassengerListResponseDTO> convertToUserListResponseDTO(List<Passenger> passengers){
        List<PassengerListResponseDTO> passengerListResponseDTOS = new ArrayList<PassengerListResponseDTO>();
        for(Passenger p:passengers){
            passengerListResponseDTOS.add(new PassengerListResponseDTO(p));
        }
        return passengerListResponseDTOS;
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
