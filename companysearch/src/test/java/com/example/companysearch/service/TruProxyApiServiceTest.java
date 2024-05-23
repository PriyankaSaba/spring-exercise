package com.example.companysearch.service;

import com.example.companysearch.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TruProxyApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TruProxyApiService truProxyApiService;

    private final String apiKey = "test-api-key";

    @BeforeEach
    public void setup() {
        
    }

    @Test
    public void testSearchCompanies_Success() {
        String companyName = "BBC LIMITED";
        String companyNumber = "06500244";
        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName(companyName);
        request.setCompanyNumber(companyNumber);

        CompanySearchResponse mockSearchResponse = mockCompanySearchResponse();
        OfficersResponse mockOfficersResponse = mockOfficersResponse();

        when(restTemplate.getForObject(any(URI.class), eq(CompanySearchResponse.class)))
                .thenReturn(mockSearchResponse);
        when(restTemplate.getForObject(any(URI.class), eq(OfficersResponse.class)))
                .thenReturn(mockOfficersResponse);

        CompanySearchResponse response = truProxyApiService.searchCompanies(apiKey, request, true);

        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertEquals(companyNumber, response.getItems().get(0).getCompanyNumber());
        assertFalse(response.getItems().get(0).getOfficers().isEmpty());
        assertEquals("BOXALL, Sarah Victoria", response.getItems().get(0).getOfficers().get(0).getName());

        verify(restTemplate, times(1)).getForObject(any(URI.class), eq(CompanySearchResponse.class));
        verify(restTemplate, times(1)).getForObject(any(URI.class), eq(OfficersResponse.class));
    }

    @Test
    public void testSearchCompanies_ClientErrorException() {
        String companyName = "BBC LIMITED";
        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName(companyName);

        when(restTemplate.getForObject(any(URI.class), eq(CompanySearchResponse.class)))
                .thenThrow(HttpClientErrorException.class);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            truProxyApiService.searchCompanies(apiKey, request, true);
        });

        assertTrue(exception.getMessage().contains("Error fetching data from TruProxyAPI"));

        verify(restTemplate, times(1)).getForObject(any(URI.class), eq(CompanySearchResponse.class));
        verify(restTemplate, never()).getForObject(any(URI.class), eq(OfficersResponse.class));
    }

    @Test
    public void testSearchCompanies_RestClientException() {
        String companyName = "BBC LIMITED";
        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyName(companyName);

        when(restTemplate.getForObject(any(URI.class), eq(CompanySearchResponse.class)))
                .thenThrow(RestClientException.class);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            truProxyApiService.searchCompanies(apiKey, request, true);
        });

        assertTrue(exception.getMessage().contains("Error communicating with TruProxyAPI"));

        verify(restTemplate, times(1)).getForObject(any(URI.class), eq(CompanySearchResponse.class));
        verify(restTemplate, never()).getForObject(any(URI.class), eq(OfficersResponse.class));
    }

    private CompanySearchResponse mockCompanySearchResponse() {
        CompanySearchResponse response = new CompanySearchResponse();
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyNumber("06500244");
        companyDto.setCompanyType("ltd");
        companyDto.setTitle("BBC LIMITED");
        companyDto.setCompanyStatus("active");
        companyDto.setDateOfCreation("2008-02-11");
        AddressDto addressDto = new AddressDto();
        addressDto.setLocality("Retford");
        addressDto.setPostalCode("DN22 0AD");
        addressDto.setPremises("Boswell Cottage Main Street");
        addressDto.setAddressLine1("North Leverton");
        addressDto.setCountry("England");
        companyDto.setAddress(addressDto);
        response.setTotalResults(1);
        response.setItems(Collections.singletonList(companyDto));
        return response;
    }

    private OfficersResponse mockOfficersResponse() {
        OfficersResponse response = new OfficersResponse();
        OfficerDto officerDto = new OfficerDto();
        officerDto.setName("BOXALL, Sarah Victoria");
        officerDto.setOfficerRole("secretary");
        officerDto.setAppointedOn("2008-02-11");
        AddressDto addressDto = new AddressDto();
        addressDto.setPremises("5");
        addressDto.setLocality("London");
        addressDto.setAddressLine1("Cranford Close");
        addressDto.setCountry("England");
        addressDto.setPostalCode("SW20 0DP");
        officerDto.setAddress(addressDto);
        response.setItems(Collections.singletonList(officerDto));
        return response;
    }
}

