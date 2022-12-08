package com.example.topgoback.Users.DTO;

import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.example.topgoback.Tools.PaginatedResponse;

import java.util.ArrayList;

public class UserMessagesListDTO extends PaginatedResponse{
    private ArrayList<UserMessagesDTO> results;

    public UserMessagesListDTO(){

    }
    public ArrayList<UserMessagesDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<UserMessagesDTO> results) {
        this.results = results;
    }
}
