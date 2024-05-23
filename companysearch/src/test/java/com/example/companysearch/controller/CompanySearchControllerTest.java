package com.example.companysearch.controller;

import com.example.companysearch.dto.CompanySearchRequest;
import com.example.companysearch.dto.CompanySearchResponse;
import com.example.companysearch.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class CompanySearchControllerTest {

	@InjectMocks
	private CompanySearchController companySearchController;

	@Mock
	private CompanyService companyService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testSearchCompanies() {
		String apiKey = "test-api-key";
		boolean activeOnly = true;
		CompanySearchRequest request = new CompanySearchRequest();
		CompanySearchResponse serviceResponse = new CompanySearchResponse();
		when(companyService.searchCompanies(eq(apiKey), any(CompanySearchRequest.class), eq(activeOnly)))
				.thenReturn(serviceResponse);

		ResponseEntity<CompanySearchResponse> responseEntity = companySearchController.searchCompanies(apiKey,
				activeOnly, request);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
}
