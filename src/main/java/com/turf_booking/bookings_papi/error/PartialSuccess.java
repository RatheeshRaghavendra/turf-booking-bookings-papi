package com.turf_booking.bookings_papi.error;

public class PartialSuccess extends CustomTurfBookingException {
	public PartialSuccess(String message) {
		super(message);
	}
	public PartialSuccess(Exception exception, String message) {
		super(exception, message);
	}
	
	

}
