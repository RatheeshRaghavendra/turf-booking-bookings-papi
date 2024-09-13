package com.turf_booking.bookings_papi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.turf_booking.bookings_papi.feign.BookingInterface;
import com.turf_booking.bookings_papi.feign.TurfInterface;
import com.turf_booking.bookings_papi.feign.UserInterface;
import com.turf_booking.bookings_papi.model.ApiError;
import com.turf_booking.bookings_papi.model.ApiResponse;
import com.turf_booking.bookings_papi.model.Slot;
import com.turf_booking.bookings_papi.model.Turf;
import com.turf_booking.bookings_papi.model.TurfWrapper;

@Service
public class TurfBookingService {

	@Autowired
	TurfInterface turfInterface;
	
	@Autowired
	BookingInterface bookingInterface;
	
	@Autowired
	UserInterface userInterface;
	
	public ResponseEntity<ApiResponse<List<TurfWrapper>>> getAllTurfs() {
		
		ApiResponse<List<TurfWrapper>> apiResponse = new ApiResponse<>();
		ApiError apiError = new ApiError();
		String customError = "";
		
		List<TurfWrapper> turfResponse = new ArrayList<>();
		
		try {			
			List<Turf> turfList = turfInterface
					.getAllTurf()
					.getBody().
					getPayload();
			
			for(Turf turf: turfList) {
				
				List<Slot> slotList = turfInterface
						.getSlots(turf.getBookedSlotIds())
						.getBody()
						.getPayload();
				
				turfResponse.add(
						new TurfWrapper(
								turf.getTurfId(),
								turf.getName(),
								turf.getCity(),
								turf.getArea(),
								turf.getAddress(),
								turf.getSports(),
								slotList,
								turf.getPricePerHour()));
			}
			
			apiResponse.setPayload(turfResponse);
			
		} catch (Exception e) {
			
			customError = "Error getting all the Turfs";
			
			apiError.setApiErrorDetails(e, customError);
			
			apiResponse.setApiError(apiError);
			apiResponse.setStatusCode(500);
		}
		
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

}
