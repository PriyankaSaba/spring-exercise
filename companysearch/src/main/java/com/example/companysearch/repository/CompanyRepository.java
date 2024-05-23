package com.example.companysearch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.companysearch.model.Company;

	public interface CompanyRepository extends JpaRepository<Company, String> {
	}


