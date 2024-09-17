package com.turf_booking.bookings_papi.model;

import lombok.Data;

import java.util.List;

@Data
public class BookingDto {

    Integer turfId;
    List<Slot> slotList;
    Integer userId;
}
