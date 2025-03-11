package com.turf_booking.bookings_papi.controller;


import java.util.List;

import com.turf_booking.bookings_papi.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turf_booking.bookings_papi.feign.BookingInterface;
import com.turf_booking.bookings_papi.feign.TurfInterface;
import com.turf_booking.bookings_papi.feign.UserInterface;
import com.turf_booking.bookings_papi.service.TurfBookingService;

@RestController
@RequestMapping("api/turf-booking")
public class TurfBookingsController {
	
	@Autowired
	TurfInterface turfInterface;
	
	@Autowired
	BookingInterface bookingInterface;
	
	@Autowired
	UserInterface userInterface;
	
	@Autowired
	TurfBookingService turfBookingService;
	
	@GetMapping("live")
	public ResponseEntity<String> getHealth() {
		return new ResponseEntity<>("live",HttpStatus.OK);
	}
	
	@GetMapping("ready")
	public ResponseEntity<String> getReady() {
		
		try {
		turfInterface.getHealth();
		bookingInterface.getHealth();
		userInterface.getHealth();
		
		return new ResponseEntity<>("Ready",HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>("Failure",HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@GetMapping("turf/all")
	public ResponseEntity<ApiResponse<List<TurfWrapper>>> getAllTurfs (){
		
		return turfBookingService.getAllTurfs();
	}
	
	@GetMapping("turf/{turfId}")
	public ResponseEntity<ApiResponse<List<Slot>>> getBookedTurfSlots(@PathVariable Integer turfId) { 
		
		return turfBookingService.getBookedTurfSlots(turfId);
	}

	@GetMapping("turf")
	public ResponseEntity<ApiResponse<List<Turf>>> searchTurfsBy (@RequestParam String parameter, @RequestParam String value){

		return turfBookingService.searchTurfBy(parameter,value);
	}

	@PostMapping("booking")
	public ResponseEntity<ApiResponse<String>> createBooking (@RequestBody BookingDto bookingDto){

		return turfBookingService.addBooking(bookingDto);
	}
	
	@GetMapping("booking/{bookingId}")
	public ResponseEntity<ApiResponse<BookingWrapper>> getBooking (@PathVariable Integer bookingId){
		
		return turfBookingService.getBooking(bookingId);
	}

	@GetMapping("booking")
	public ResponseEntity<ApiResponse<List<BookingWrapper>>> searchBookingsBy (@RequestParam String parameter, @RequestParam Integer value){

		return turfBookingService.searchBookingsBy(parameter,value);
	}

	@DeleteMapping("booking/{bookingId}")
	public ResponseEntity<ApiResponse<String>> cancelBooking (@PathVariable Integer bookingId){
		
		return turfBookingService.cancelBooking(bookingId);
	}

	@GetMapping("checkout")
	public ResponseEntity<ApiResponse<Checkout>> getCheckout (@RequestParam String username, @RequestParam Integer turfId, @RequestParam List<String> slotIds){
		return turfBookingService.getCheckout(turfId,slotIds,username);
	}

	@PostMapping("user")
	public ResponseEntity<ApiResponse<String>> addUser (@RequestBody UserDetails userDetails){

		return turfBookingService.addUser(userDetails);
	}

}
