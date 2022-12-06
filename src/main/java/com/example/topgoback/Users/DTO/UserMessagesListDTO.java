package com.example.topgoback.Users.DTO;

import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Tools.PaginatedResponse;

import java.util.ArrayList;

public class UserMessagesListDTO {
    private PaginatedResponse totalCount;

    private ArrayList<Message> results;


    public UserMessagesListDTO(){
        totalCount = new PaginatedResponse();
    }
    public int getTotalCount() {
        return totalCount.getTotalCount();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }

    public ArrayList<Message> getResults() {
        return results;
    }

    public void setResults(ArrayList<Message> results) {
        this.results = results;
    }
}
