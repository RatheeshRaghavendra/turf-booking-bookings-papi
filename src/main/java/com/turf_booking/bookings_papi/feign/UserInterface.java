package com.turf_booking.bookings_papi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.turf_booking.bookings_papi.model.ApiResponse;
import com.turf_booking.bookings_papi.model.UserDetails;

@FeignClient("USER-SAPI")
public interface UserInterface {
	
	@GetMapping("api/user-sapi/live")
	public String getHealth();
	
	@GetMapping("api/user-sapi/user/{userId}")
	public ResponseEntity<ApiResponse<UserDetails>> getUserById (@PathVariable Integer userId);
	
	@PostMapping("api/user-sapi/user")
	public ResponseEntity<ApiResponse<String>> addUser (@RequestBody UserDetails user);
	
	@PutMapping("api/user-sapi/user")
	public ResponseEntity<ApiResponse<String>> updateUser (@RequestBody UserDetails user);
		
	@DeleteMapping("api/user-sapi/user/{userId}")
	public ResponseEntity<ApiResponse<String>> deleteUser (@PathVariable Integer userId);

}
