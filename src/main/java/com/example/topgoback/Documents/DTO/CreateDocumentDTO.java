package com.example.topgoback.Documents.DTO;

import javax.validation.constraints.NotNull;

public class CreateDocumentDTO {

    @NotNull
    private String name;
    @NotNull
    private String documentImage;

    public CreateDocumentDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

}
