package com.example.companysearch.dto;
import lombok.Data;

@Data
public class OfficerDto {
    private String name;
    private String officerRole;
    private String appointedOn;
    private String resignedOn;
    private AddressDto address;
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOfficerRole() {
		return officerRole;
	}
	public void setOfficerRole(String officerRole) {
		this.officerRole = officerRole;
	}
	public String getAppointedOn() {
		return appointedOn;
	}
	public void setAppointedOn(String appointedOn) {
		this.appointedOn = appointedOn;
	}
	public AddressDto getAddress() {
		return address;
	}
	public void setAddress(AddressDto address) {
		this.address = address;
	}
	public String getResignedOn() {
		return resignedOn;
	}
	public void setResignedOn(String resignedOn) {
		this.resignedOn = resignedOn;
	}
}