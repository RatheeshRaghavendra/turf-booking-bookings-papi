package com.turf_booking.bookings_papi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookingsPapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingsPapiApplication.class, args);
	}

}
