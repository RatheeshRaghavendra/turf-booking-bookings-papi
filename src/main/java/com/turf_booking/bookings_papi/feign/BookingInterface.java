package com.turf_booking.bookings_papi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("BOOKING-SAPI")
public interface BookingInterface {
	
	@GetMapping("api/booking/live")
	public String getHealth();

}
