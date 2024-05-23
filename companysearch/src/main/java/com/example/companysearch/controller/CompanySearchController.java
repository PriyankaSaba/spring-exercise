package com.example.companysearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.companysearch.dto.CompanySearchRequest;
import com.example.companysearch.dto.CompanySearchResponse;
import com.example.companysearch.service.CompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanySearchController {
    
	@Autowired
	private CompanyService companyService;

    @PostMapping("/search")
    public ResponseEntity<CompanySearchResponse> searchCompanies(
            @RequestHeader("x-api-key") String apiKey,
            @RequestParam(defaultValue = "true") boolean activeOnly,
            @Valid @RequestBody CompanySearchRequest request) {
        CompanySearchResponse response = companyService.searchCompanies(apiKey, request, activeOnly);
        return ResponseEntity.ok(response);
    }
}
