package com.example.companysearch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.example.companysearch.dto.*;
import com.example.companysearch.model.*;
import com.example.companysearch.repository.*;

public class CompanyServiceTest {
	
	 @InjectMocks
	    private CompanyService companyService;

	    @Mock
	    private TruProxyApiService truProxyApiService;

	    @Mock
	    private CompanyRepository companyRepository;

	    @Mock
	    private OfficerRepository officerRepository;

	    public CompanyServiceTest() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testSearchCompanies_WhenCompanyInDatabase() {
	        String companyNumber = "06500244";
	        Company company = new Company();
	        company.setCompanyNumber(companyNumber);
	        when(companyRepository.findById(companyNumber)).thenReturn(Optional.of(company));

	        CompanySearchRequest request = new CompanySearchRequest();
	        request.setCompanyNumber(companyNumber);
	        CompanySearchResponse response = companyService.searchCompanies("api-key", request, true);

	        assertNotNull(response);
	        assertEquals(1, response.getTotalResults());
	        verify(truProxyApiService, never()).searchCompanies(anyString(), any(), anyBoolean());
	    }

	    @Test
	    public void testSearchCompanies_WhenCompanyNotInDatabase() {
	    	
	        String companyNumber = "06500244";
	        CompanySearchRequest request = new CompanySearchRequest();
	        request.setCompanyNumber(companyNumber);
	        CompanySearchResponse apiResponse = new CompanySearchResponse();
	        apiResponse.setTotalResults(1);
	        apiResponse.setItems(Collections.singletonList(new CompanyDto()));
	        when(companyRepository.findById(companyNumber)).thenReturn(Optional.empty());
	        when(truProxyApiService.searchCompanies(anyString(), any(), anyBoolean())).thenReturn(apiResponse);

	        CompanySearchResponse response = companyService.searchCompanies("api-key", request, true);

	        assertNotNull(response);
	        assertEquals(1, response.getTotalResults());
	        verify(truProxyApiService).searchCompanies(anyString(), any(), anyBoolean());
	    }

}
