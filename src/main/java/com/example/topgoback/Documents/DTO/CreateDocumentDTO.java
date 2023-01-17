package com.example.topgoback.Documents.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateDocumentDTO {

    @NotNull(message = "is required!")
    @Size(max = 100)
    private String name;
    @NotNull(message = "is required!")
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
