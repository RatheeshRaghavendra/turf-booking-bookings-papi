package com.turf_booking.bookings_papi.error;

public class SlotAlreadyBooked extends CustomTurfBookingException {
    public SlotAlreadyBooked(String message) {
        super(message);
    }
    public SlotAlreadyBooked(Exception e, String message){
        super(e, message);
    }
}
