package com.turf_booking.bookings_papi.model;

import lombok.Data;

@Data
public class Slot {
	
	String slotId;
	String day;
	String month;
	String year;
	String slotTime;
	
	public Slot(String day, String month, String year, String slotTime) {
		super();
		
		this.slotId = day + "-" + month + "-" + year + "$" + slotTime;
		this.day = day;
		this.month = month;
		this.year = year;
		this.slotTime = slotTime;
	}

	public Slot() {
		super();
		
	}

	public void setSlotId (){

		this.slotId = this.day + "-" + this.month + "-" + this.year + "$" + this.slotTime;
//		return this;
	}
	
}
