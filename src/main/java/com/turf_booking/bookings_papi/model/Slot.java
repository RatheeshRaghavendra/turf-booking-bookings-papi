package com.turf_booking.bookings_papi.model;

import lombok.Data;

@Data
public class Slot {
	
	Integer slotId;
	String day;
	String month;
	String year;
	String slotTime;
	
	public Slot(String day, String month, String year, String slotTime) {
		super();
		
		this.day = day;
		this.month = month;
		this.year = year;
		this.slotTime = slotTime;
	}

	public Slot() {
		super();
		
	}
	
	
	
	
}
