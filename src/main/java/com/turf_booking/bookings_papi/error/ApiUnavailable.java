package com.turf_booking.bookings_papi.error;

public class ApiUnavailable extends CustomTurfBookingException {
    public ApiUnavailable(String message) {
        super(message);
    }
    public ApiUnavailable(Exception e, String message){
        super(e, message);
    }
}
