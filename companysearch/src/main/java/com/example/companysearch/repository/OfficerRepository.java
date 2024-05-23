package com.example.companysearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.companysearch.model.Officer;

public interface OfficerRepository extends JpaRepository<Officer, Long> {
}