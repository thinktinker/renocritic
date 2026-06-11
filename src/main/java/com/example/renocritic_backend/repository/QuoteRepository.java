package com.example.renocritic_backend.repository;

import com.example.renocritic_backend.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository <Quote, Integer> {

    // Derived Query:
    List<Quote> findByUserId(Integer id);

}

