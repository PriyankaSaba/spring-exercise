package com.example.companysearch.dto;

import java.util.List;

import lombok.Data;

@Data
public class OfficersResponse {
    private List<OfficerDto> items;

	public List<OfficerDto> getItems() {
		return items;
	}

	public void setItems(List<OfficerDto> items) {
		this.items = items;
	}
}
