package com.example.companysearch.integration;
import com.example.companysearch.dto.*;
import com.example.companysearch.dto.CompanySearchResponse;
import com.example.companysearch.dto.OfficersResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CompanySearchIntegrationTest {

    private WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
        configureFor("localhost", 8081);
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void testSearchCompanies() throws Exception {
        // Mock TruProxyAPI response
        stubFor(get(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Search"))
                .withQueryParam("Query", equalTo("06500244"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(mockCompanySearchResponse()))));

        stubFor(get(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Officers"))
                .withQueryParam("CompanyNumber", equalTo("06500244"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(mockOfficersResponse()))));

        // Prepare request
        CompanySearchRequest request = new CompanySearchRequest();
        request.setCompanyNumber("06500244");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "test-api-key");

        HttpEntity<CompanySearchRequest> entity = new HttpEntity<>(request, headers);

        // Execute request
        ResponseEntity<CompanySearchResponse> response = restTemplate.exchange(
                "/api/companies/search",
                HttpMethod.POST,
                entity,
                CompanySearchResponse.class
        );

        // Assert response
        assertEquals(200, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalResults());
        assertEquals("06500244", response.getBody().getItems().get(0).getCompanyNumber());
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

