package com.example.companysearch.model;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String officerRole;
    private String appointedOn;
    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "company_number")
    private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}