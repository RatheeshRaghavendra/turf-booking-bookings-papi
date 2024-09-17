package com.turf_booking.bookings_papi.service;

import java.util.ArrayList;
import java.util.List;

import com.turf_booking.bookings_papi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.turf_booking.bookings_papi.feign.BookingInterface;
import com.turf_booking.bookings_papi.feign.TurfInterface;
import com.turf_booking.bookings_papi.feign.UserInterface;

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

	public ResponseEntity<ApiResponse<String>> addBooking(BookingDto bookingDto) {

		ApiResponse<String> apiResponse = new ApiResponse<>();
		ApiError apiError = new ApiError();
		String customError = "";

		try {

			List<Slot> slotList = bookingDto.getSlotList();
			System.out.println(slotList);
			List<String> slotIdList = new ArrayList<>();
 			for (Slot slot : slotList) {
				slot.setSlotId();
				System.out.println("Slot ID: " + slot.getSlotId());
				slotIdList.add(slot.getSlotId());
			}

			turfInterface.bookTurf(bookingDto.getTurfId(), slotIdList);
			bookingInterface.addBooking(new Booking(bookingDto.getTurfId(),slotIdList,bookingDto.getUserId()));

			apiResponse.setPayload("success");

		} catch (Exception e) {
			e.printStackTrace();

			apiError.setApiErrorDetails(e,customError);
			apiResponse.setApiError(apiError);
			apiResponse.setStatusCode(500);
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}
}
