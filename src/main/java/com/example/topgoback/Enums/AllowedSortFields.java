package com.example.topgoback.Enums;

public enum AllowedSortFields {
    ID("id"),
    START("start"),
    PRICE("price");

    private final String field;

    AllowedSortFields(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}