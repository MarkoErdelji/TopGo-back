package com.example.topgoback.Tools;

public class PaginatedResponse {
    private int totalCount;

    public PaginatedResponse(int size) {
        this.totalCount = size;
    }

    public PaginatedResponse() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
