package com.example.companysearch.dto;

import java.util.List;

import lombok.Data;

@Data
public class CompanySearchResponse {
    private int totalResults;
    private List<CompanyDto> items;
	public int getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}
	public List<CompanyDto> getItems() {
		return items;
	}
	public void setItems(List<CompanyDto> items) {
		this.items = items;
	}
}
