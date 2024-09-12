package com.turf_booking.bookings_papi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("TURF-SAPI")
public interface TurfInterface {
	
	@GetMapping("api/turf/live")
	public String getHealth();

}
