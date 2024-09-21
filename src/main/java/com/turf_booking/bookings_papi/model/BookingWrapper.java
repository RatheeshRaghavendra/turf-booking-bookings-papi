package com.turf_booking.bookings_papi.model;

import java.util.List;

import lombok.Data;

@Data
public class BookingWrapper {
	
	Integer bookingId;
	String turfName;
	String turfAddress;
	List<Slot> bookedSlots;
	String userName;
	String userUserName;
	
	public BookingWrapper(Integer bookingId, String turfName, String turfAddress, List<Slot> bookedSlots,
			String userName, String userUserName) {
		super();
		this.bookingId = bookingId;
		this.turfName = turfName;
		this.turfAddress = turfAddress;
		this.bookedSlots = bookedSlots;
		this.userName = userName;
		this.userUserName = userUserName;
	}

	public BookingWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
