package com.example.topgoback.RejectionLetters.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RejectionLetterDTO {
    private String reason;
    private LocalDateTime timeOfRejection;

    public RejectionLetterDTO() {}

    public static RejectionLetterDTO getMockupData(){
        RejectionLetterDTO rejectionLetterDTO = new RejectionLetterDTO();

        rejectionLetterDTO.reason = ("Ride is canceled due to previous problems with the passenger");
        String str = "2022-11-25T17:32:28Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        rejectionLetterDTO.timeOfRejection = dateTime;

        return rejectionLetterDTO;
    }
}
