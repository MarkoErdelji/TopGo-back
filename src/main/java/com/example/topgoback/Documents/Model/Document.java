package com.example.topgoback.Documents.Model;

import com.example.topgoback.Users.Model.Driver;
import jakarta.persistence.*;

@Entity
public class Document {
    @Id
    @SequenceGenerator(name = "mySeqGenDoc", sequenceName = "mySeqGenDoc", initialValue = 4, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenDoc")
    @Column(name="id")
    private int id;
    private String name;
    @Column(length = 500000)
    private String documentImage;
    @ManyToOne
    private Driver driver;

    public Document() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
