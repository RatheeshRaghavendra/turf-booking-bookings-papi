package com.turf_booking.bookings_papi.error;

public class UnexpectedTurfBookingException extends CustomTurfBookingException {
    public UnexpectedTurfBookingException(String message) {
        super(message);
    }
    public UnexpectedTurfBookingException(Exception exception, String message) {
        super(exception, message);
    }
}
