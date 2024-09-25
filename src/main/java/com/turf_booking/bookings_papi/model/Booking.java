package com.turf_booking.bookings_papi.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Booking {

	Integer bookingId;
	Integer turfId;
	List<String> slotIds;
	Integer userId;
	Integer price = 0;
	
	public Booking(Integer turfId, List<String> slotIds, Integer userId, Integer price) {
		super();

		this.turfId = turfId;
		this.slotIds = slotIds;
		this.userId = userId;
		this.price = price;
	}
	
	
}
