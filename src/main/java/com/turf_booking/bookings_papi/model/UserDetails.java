package com.turf_booking.bookings_papi.model;

import lombok.Data;

@Data

public class UserDetails {

	Integer userId;
	String username;
	String password;
	String firstName;
	String lastName;
	String phoneNumber;
	String emailId;
	
	public UserDetails(Integer userId, String username, String password, String firstName, String lastName, String phoneNumber,
			String emailId) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.emailId = emailId;
	}

	public UserDetails() {
		super();
	}
	
}
