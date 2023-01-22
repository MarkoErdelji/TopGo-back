package com.example.topgoback.Enums;

public enum AllowedSortFields {
    ID("id"),
    START("start"),
    END("end"),
    PRICE("price"),
    ROUTE("route");

    private final String field;

    AllowedSortFields(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}