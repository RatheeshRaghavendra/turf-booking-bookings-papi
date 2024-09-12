package com.turf_booking.bookings_papi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turf_booking.bookings_papi.feign.BookingInterface;
import com.turf_booking.bookings_papi.feign.TurfInterface;

@RestController
@RequestMapping("api/turf-booking")
public class TurfBookingsController {
	
	@Autowired
	TurfInterface turfInterface;
	
	@Autowired
	BookingInterface bookingInterface;
	
	
	@GetMapping("live")
	public ResponseEntity<String> getHealth() {
		return new ResponseEntity<>("live",HttpStatus.OK);
	}
	
	@GetMapping("ready")
	public ResponseEntity<String> getReady() {
		
		try {
		String turfReady = turfInterface.getHealth();
		String bookingReady = bookingInterface.getHealth();
		
		return new ResponseEntity<>("Ready",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>("Failure",HttpStatus.SERVICE_UNAVAILABLE);
	}

}
