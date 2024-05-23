package com.example.companysearch.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.example.companysearch.dto.AddressDto;
import com.example.companysearch.dto.CompanyDto;
import com.example.companysearch.dto.CompanySearchRequest;
import com.example.companysearch.dto.CompanySearchResponse;
import com.example.companysearch.dto.OfficerDto;
import com.example.companysearch.model.Address;
import com.example.companysearch.model.Company;
import com.example.companysearch.model.Officer;
import com.example.companysearch.repository.CompanyRepository;
import com.example.companysearch.repository.OfficerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class CompanyService {
	
   @Autowired
   TruProxyApiService truProxyApiService;
   @Autowired
   CompanyRepository companyRepository;
   @Autowired
   OfficerRepository officerRepository;

    @Transactional
    public CompanySearchResponse searchCompanies(String apiKey, CompanySearchRequest request, boolean activeOnly) {
    	try {
            if (request.getCompanyNumber() != null && !request.getCompanyNumber().isEmpty()) {
                Optional<Company> optionalCompany = companyRepository.findById(request.getCompanyNumber());
                if (optionalCompany.isPresent()) {
                    Company company = optionalCompany.get();
                    return buildResponseFromCompany(company);
                }
            }

            CompanySearchResponse apiResponse = truProxyApiService.searchCompanies(apiKey, request, activeOnly);

            if (request.getCompanyNumber() != null && !request.getCompanyNumber().isEmpty()) {
                saveCompanyAndOfficers(apiResponse.getItems());
            }

            return apiResponse;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error fetching data from TruProxyAPI: " + e.getStatusCode());
        } catch (RestClientException e) {
            throw new RuntimeException("Error communicating with TruProxyAPI: " + e.getMessage());
        }
    }

    private void saveCompanyAndOfficers(List<CompanyDto> companyDtos) {
        for (CompanyDto companyDto : companyDtos) {
            Company company = new Company();
            company.setCompanyNumber(companyDto.getCompanyNumber());
            company.setCompanyType(companyDto.getCompanyType());
            company.setTitle(companyDto.getTitle());
            company.setCompanyStatus(companyDto.getCompanyStatus());
            company.setDateOfCreation(companyDto.getDateOfCreation());
            company.setAddress(convertAddressDtoToEntity(companyDto.getAddress()));

            List<Officer> officers = companyDto.getOfficers().stream()
                    .map(this::convertOfficerDtoToEntity)
                    .peek(officer -> officer.setCompany(company))
                    .collect(Collectors.toList());

            company.setOfficers(officers);
            companyRepository.save(company);
        }
    }

    private Address convertAddressDtoToEntity(AddressDto addressDto) {
        Address address = new Address();
        address.setLocality(addressDto.getLocality());
        address.setPostalCode(addressDto.getPostalCode());
        address.setPremises(addressDto.getPremises());
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setCountry(addressDto.getCountry());
        return address;
    }

    private Officer convertOfficerDtoToEntity(OfficerDto officerDto) {
        Officer officer = new Officer();
        officer.setName(officerDto.getName());
        officer.setOfficerRole(officerDto.getOfficerRole());
        officer.setAppointedOn(officerDto.getAppointedOn());
        officer.setAddress(convertAddressDtoToEntity(officerDto.getAddress()));
        return officer;
    }

    private CompanySearchResponse buildResponseFromCompany(Company company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyNumber(company.getCompanyNumber());
        companyDto.setCompanyType(company.getCompanyType());
        companyDto.setTitle(company.getTitle());
        companyDto.setCompanyStatus(company.getCompanyStatus());
        companyDto.setDateOfCreation(company.getDateOfCreation());
        companyDto.setAddress(convertAddressEntityToDto(company.getAddress()));
        companyDto.setOfficers(company.getOfficers().stream()
                .map(this::convertOfficerEntityToDto)
                .collect(Collectors.toList()));

        CompanySearchResponse response = new CompanySearchResponse();
        response.setTotalResults(1);
        response.setItems(List.of(companyDto));
        return response;
    }

    private AddressDto convertAddressEntityToDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setLocality(address.getLocality());
        addressDto.setPostalCode(address.getPostalCode());
        addressDto.setPremises(address.getPremises());
        addressDto.setAddressLine1(address.getAddressLine1());
        addressDto.setCountry(address.getCountry());
        return addressDto;
    }

    private OfficerDto convertOfficerEntityToDto(Officer officer) {
        OfficerDto officerDto = new OfficerDto();
        officerDto.setName(officer.getName());
        officerDto.setOfficerRole(officer.getOfficerRole());
        officerDto.setAppointedOn(officer.getAppointedOn());
        officerDto.setAddress(convertAddressEntityToDto(officer.getAddress()));
        return officerDto;
    }
    }



