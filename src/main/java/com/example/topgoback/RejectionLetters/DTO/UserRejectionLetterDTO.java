package com.example.topgoback.RejectionLetters.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserRejectionLetterDTO {

    public String reason;
    public LocalDateTime timeOfRejection;


    public UserRejectionLetterDTO() {
    }

    public static UserRejectionLetterDTO getMockupData(){
        UserRejectionLetterDTO userRejectionLetterDTO = new UserRejectionLetterDTO();

        userRejectionLetterDTO.reason = ("Ride is canceled due to previous problems with the passenger");
        String str = "2022-11-25T17:32:28Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        userRejectionLetterDTO.timeOfRejection = dateTime;

        return userRejectionLetterDTO;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(LocalDateTime timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }
}
