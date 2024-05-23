package com.example.companysearch.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CompanySearchRequest {
    @NotEmpty
    private String companyName;
    private String companyNumber;
	
    public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}
}
