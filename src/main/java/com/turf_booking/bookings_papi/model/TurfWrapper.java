package com.turf_booking.bookings_papi.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TurfWrapper {
	
	Integer turfId;
	String name;
	String city;
	String area;
	String address;
	String sports;
	List<Slot> bookedSlots;
	Integer pricePerHour;
	
	public TurfWrapper(Integer turfId, String name, String city, String area, String address, String sports,
			List<Slot> bookedSlots, Integer pricePerHour) {
		super();
		this.turfId = turfId;
		this.name = name;
		this.city = city;
		this.area = area;
		this.address = address;
		this.sports = sports;
		this.bookedSlots = bookedSlots;
		this.pricePerHour = pricePerHour;
	}

	public TurfWrapper() {
		super();
	}
	
	

}
