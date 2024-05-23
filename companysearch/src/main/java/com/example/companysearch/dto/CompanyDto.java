package com.example.companysearch.dto;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDto {
    private String companyNumber;
    private String companyType;
    private String title;
    private String companyStatus;
    private String dateOfCreation;
    private AddressDto address;
    private List<OfficerDto> officers;
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCompanyStatus() {
		return companyStatus;
	}
	public void setCompanyStatus(String companyStatus) {
		this.companyStatus = companyStatus;
	}
	public String getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(String dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public AddressDto getAddress() {
		return address;
	}
	public void setAddress(AddressDto address) {
		this.address = address;
	}
	public List<OfficerDto> getOfficers() {
		return officers;
	}
	public void setOfficers(List<OfficerDto> officers) {
		this.officers = officers;
	}
}
