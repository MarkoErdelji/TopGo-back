package com.example.topgoback.Panic.Repository;

import com.example.topgoback.Panic.Model.Panic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PanicRepository extends JpaRepository<Panic,Integer> {
    Page<Panic> findAll(Pageable pageable);
}
