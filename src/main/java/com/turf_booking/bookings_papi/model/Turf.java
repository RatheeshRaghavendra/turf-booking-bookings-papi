package com.turf_booking.bookings_papi.model;

import java.util.List;

import lombok.Data;

@Data
public class Turf {
	
	Integer turfId;
	String name;
	String city;
	String area;
	String address;
	String sports;
	List<Integer> bookedSlotIds;
	
	Integer pricePerHour;
	
	public Turf(Integer turfId, String name, String city, String area, String address, String sports,
			List<Integer> bookedSlotIds, Integer pricePerHour) {
		super();
		this.turfId = turfId;
		this.name = name;
		this.city = city;
		this.area = area;
		this.address = address;
		this.sports = sports;
		this.bookedSlotIds = bookedSlotIds;
		this.pricePerHour = pricePerHour;
	}

	public Turf() {
		super();
	}
	
	

}
