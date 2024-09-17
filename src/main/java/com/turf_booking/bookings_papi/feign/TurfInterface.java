package com.turf_booking.bookings_papi.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.turf_booking.bookings_papi.model.ApiResponse;
import com.turf_booking.bookings_papi.model.Slot;
import com.turf_booking.bookings_papi.model.Turf;



@FeignClient("TURF-SAPI")
public interface TurfInterface {
	
	@GetMapping("api/turf-sapi/live")
	public String getHealth();
	
	@GetMapping("api/turf-sapi/turf/{turfId}")
	public ResponseEntity<ApiResponse<Turf>> getTurfById (@PathVariable Integer turfId);
	
	@GetMapping("api/turf-sapi/turf/search-by")
	public ResponseEntity<ApiResponse<List<Turf>>> getTurfsByParameter (@RequestParam String parameter, @RequestParam String value);
	
	@GetMapping("api/turf-sapi/turf/all")
	public ResponseEntity<ApiResponse<List<Turf>>> getAllTurf();
	
	@PostMapping("api/turf-sapi/turf")
	public ResponseEntity<ApiResponse<String>> addTurf (@RequestBody Turf turf);
	
	@PatchMapping("api/turf-sapi/turf/book/{turfId}")
	public ResponseEntity<ApiResponse<String>> bookTurf (@PathVariable Integer turfId,@RequestParam List<String> slotIds);
	
	@DeleteMapping("api/turf-sapi/turf/cancel/{turfId}")
	public ResponseEntity<ApiResponse<String>> cancelTurf (@PathVariable Integer turfId,@RequestParam List<String> slotIds);
	
	@GetMapping("api/turf-sapi/slots")
	public ResponseEntity<ApiResponse<List<Slot>>> getSlots (@RequestParam List<Integer> slots);
	
	@GetMapping("api/turf-sapi/slots/all")
	public ResponseEntity<ApiResponse<List<Slot>>> getAllSlots ();
	
	@PostMapping("api/turf-sapi/slots")
	public ResponseEntity<ApiResponse<String>> addSlots (@RequestBody List<Slot> slots);

}
