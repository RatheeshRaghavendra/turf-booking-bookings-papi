package com.turf_booking.bookings_papi.error;

import lombok.Getter;

@Getter
public class CustomTurfBookingException extends RuntimeException {

    Exception exception;
    String customMessage;

    public CustomTurfBookingException(String message) {
        super(message);
    }
    public CustomTurfBookingException(Exception exception, String message){
        super(message);
        this.customMessage = message;
        this.exception = exception;
    }
}
