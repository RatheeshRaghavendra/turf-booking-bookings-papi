package com.turf_booking.bookings_papi.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.turf_booking.bookings_papi.model.ApiResponse;
import com.turf_booking.bookings_papi.model.Booking;


@FeignClient("BOOKING-SAPI")
public interface BookingInterface {
	
	@GetMapping("api/booking-sapi/live")
	public String getHealth();
	
	@PostMapping("api/booking-sapi/booking")
	public ResponseEntity<ApiResponse<String>> addBooking (@RequestBody Booking booking);
	
	@GetMapping("api/booking-sapi/booking/{bookingId}")
	public ResponseEntity<ApiResponse<Booking>> getBooking (@PathVariable Integer bookingId);
	
	@GetMapping("api/booking-sapi/booking/search-by")
	public ResponseEntity<ApiResponse<List<Booking>>> getBookingBy (@RequestParam String parameter,@RequestParam Integer value);
	
	@GetMapping("api/booking-sapi/booking/all")
	public ResponseEntity<ApiResponse<List<Booking>>> getAllBookings ();
	
	@DeleteMapping("api/booking-sapi/booking/{bookingId}")
	public ResponseEntity<ApiResponse<String>> cancelBooking (@PathVariable Integer bookingId);

}
