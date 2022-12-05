package com.example.topgoback.Documents;

import com.example.topgoback.Documents.Model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Integer> {
}
