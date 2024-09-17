package com.turf_booking.bookings_papi.model;

import java.util.List;

import lombok.Data;

@Data
public class Booking {

	Integer bookingId;
	Integer turfId;
	List<String> slotIds;
	Integer userId;
	
	public Booking(Integer turfId, List<String> slotIds, Integer userId) {
		super();

		this.turfId = turfId;
		this.slotIds = slotIds;
		this.userId = userId;
	}

	public Booking() {
		super();
	}
	
	
	
	
}
