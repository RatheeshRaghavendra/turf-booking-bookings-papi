package com.turf_booking.bookings_papi.service;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.turf_booking.bookings_papi.model.*;

import feign.FeignException;
import jakarta.ws.rs.core.NewCookie;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.turf_booking.bookings_papi.auxillary.AuxillaryFunctions;
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
	
	@Autowired
	AuxillaryFunctions auxillaryFunctions;
	
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
				
				List<Slot> slotList = turf.getBookedSlotIds()
						.stream()
						.map(slot -> auxillaryFunctions.extractSlot(slot))
						.toList();
				
				
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
	
	public ResponseEntity<ApiResponse<List<Slot>>> getBookedTurfSlots(Integer turfId) {
		
		ApiResponse<List<Slot>> apiResponse = new ApiResponse<>();
		ApiError apiError = new ApiError();
		String customError = "";
		
		try {
			List<String> bookedSlotIds = turfInterface.getTurfById(turfId)
					.getBody()
					.getPayload()
					.getBookedSlotIds();
			List<Slot> bookedSlots = bookedSlotIds.stream()
					.map(slotId -> auxillaryFunctions.extractSlot(slotId))
					.toList();
			
			apiResponse.setPayload(bookedSlots);
		} catch (Exception e) {
			
			customError = "Error getting the booked slots";
			
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
			System.out.println(bookingDto);
			List<String> slotIdList = new ArrayList<>();
 			for (Slot slot : slotList) {
				slot.setSlotId();
				System.out.println("Slot ID: " + slot.getSlotId());
				slotIdList.add(slot.getSlotId());
			}
 			System.out.println("SlotIdLIst: " + slotIdList.toString());
			turfInterface.bookTurf(bookingDto.getTurfId(), slotIdList);
			System.out.println("Updated Turf");
			String bookingResponse = bookingInterface.addBooking(new Booking(bookingDto.getTurfId(),slotIdList,bookingDto.getUserId())).getBody().getPayload();
			System.out.println("Updated Book");

			apiResponse.setPayload(bookingResponse);

		} catch (Exception e) {
			e.printStackTrace();
			customError = "Error making the booking";
			apiError.setApiErrorDetails(e,customError);
			apiResponse.setApiError(apiError);
			apiResponse.setStatusCode(500);
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<ApiResponse<BookingWrapper>> getBooking(Integer bookingId) {
		
		ApiResponse<BookingWrapper> apiResponse = new ApiResponse<>();
		ApiError apiError = new ApiError();
		String customError = "";
		
		try {
			Booking booking = bookingInterface.getBooking(bookingId).getBody().getPayload();
			Turf turf = turfInterface.getTurfById(booking.getTurfId()).getBody().getPayload();
			List<Slot> slotList = booking.getSlotIds()
					.stream()
					.map(slotId -> auxillaryFunctions.extractSlot(slotId))
					.toList();
			UserDetails user = userInterface.getUserById(booking.getUserId()).getBody().getPayload();
			
			BookingWrapper bookingResponse = new BookingWrapper(bookingId, 
					turf.getName(), 
					turf.getAddress(), 
					slotList, 
					user.getFirstName() + " " + user.getLastName(), 
					user.getUsername());
			
			apiResponse.setPayload(bookingResponse);
			
		} catch (FeignException e) {
			e.printStackTrace();
			
			customError = "Error retrieving the Booking with the ID: " + bookingId;
			
			apiError.setApiErrorDetails(e,customError);
			apiResponse.setApiError(apiError);
			apiResponse.setStatusCode(500);
		}
		
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<String>> cancelBooking(Integer bookingId) {
		
		ApiResponse<String> apiResponse = new ApiResponse<>();
		ApiError apiError = new ApiError();
		String customError = "";
		
		try {
			
			Booking booking = bookingInterface.getBooking(bookingId).getBody().getPayload();
			
			turfInterface.cancelTurf(booking.getTurfId(), booking.getSlotIds());
			
			String bookingResponse = bookingInterface.cancelBooking(bookingId).getBody().getPayload();
			
			apiResponse.setPayload(bookingResponse);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			customError = "Error cancelling the Booking with the ID: " + bookingId;
			
			apiError.setApiErrorDetails(e,customError);
			apiResponse.setApiError(apiError);
			apiResponse.setStatusCode(500);
		}
			
		
		
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	
}
