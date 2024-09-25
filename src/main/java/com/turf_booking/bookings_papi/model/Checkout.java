package com.turf_booking.bookings_papi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Checkout {

    String username;
    String turfName;
    String turfAddress;
    List<Slot> slots;
    Integer price;
}
