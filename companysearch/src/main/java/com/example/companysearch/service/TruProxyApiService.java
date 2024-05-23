package com.example.companysearch.service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.companysearch.dto.CompanyDto;
import com.example.companysearch.dto.CompanySearchRequest;
import com.example.companysearch.dto.CompanySearchResponse;
import com.example.companysearch.dto.OfficerDto;
import com.example.companysearch.dto.OfficersResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TruProxyApiService {
	
	@Autowired
    RestTemplate restTemplate;

    private static final String BASE_URL = "https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1";

    public CompanySearchResponse searchCompanies(String apiKey, CompanySearchRequest request, boolean activeOnly) {
        try {
            URI uri = buildSearchUri(request.getCompanyName(), request.getCompanyNumber());
            CompanySearchResponse searchResponse = performGetRequest(uri, apiKey, CompanySearchResponse.class);

            if (searchResponse.getItems() != null && !searchResponse.getItems().isEmpty()) {
                for (CompanyDto company : searchResponse.getItems()) {
                    if (activeOnly && !"active".equalsIgnoreCase(company.getCompanyStatus())) {
                        continue;
                    }

                    URI officersUri = buildOfficersUri(company.getCompanyNumber());
                    OfficersResponse officersResponse = performGetRequest(officersUri, apiKey, OfficersResponse.class);
                    company.setOfficers(filterActiveOfficers(officersResponse));
                }
            }

            return searchResponse;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error fetching data from TruProxyAPI: " + e.getStatusCode());
        } catch (RestClientException e) {
            throw new RuntimeException("Error communicating with TruProxyAPI: " + e.getMessage());
        }
    }

    private URI buildSearchUri(String companyName, String companyNumber) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/Search");

        if (companyNumber != null && !companyNumber.isEmpty()) {
            builder.queryParam("Query", companyNumber);
        } else {
            builder.queryParam("Query", companyName);
        }

        return builder.build().toUri();
    }

    private URI buildOfficersUri(String companyNumber) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL + "/Officers")
                .queryParam("CompanyNumber", companyNumber)
                .build()
                .toUri();
    }

    private <T> T performGetRequest(URI uri, String apiKey, Class<T> responseType) {
        try {
            return restTemplate.getForObject(uri, responseType);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Client error when fetching data from TruProxyAPI: " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            throw new RuntimeException("Error communicating with TruProxyAPI: " + e.getMessage(), e);
        }
    }

    private List<OfficerDto> filterActiveOfficers(OfficersResponse officersResponse) {
        return officersResponse.getItems().stream()
                .filter(officer -> officer.getResignedOn() == null)
                .collect(Collectors.toList());
    }
}
