package com.turf_booking.bookings_papi.service;

import java.util.ArrayList;
import java.util.List;

import com.turf_booking.bookings_papi.error.ApiResponseException;
import com.turf_booking.bookings_papi.error.UnexpectedTurfBookingException;
import com.turf_booking.bookings_papi.logger.GlobalLog;
import com.turf_booking.bookings_papi.model.*;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.turf_booking.bookings_papi.auxillary.AuxillaryFunctions;
import com.turf_booking.bookings_papi.feign.BookingInterface;
import com.turf_booking.bookings_papi.feign.TurfInterface;
import com.turf_booking.bookings_papi.feign.UserInterface;

@Service
@Log4j2
public class TurfBookingService {

	private String prefix = GlobalLog.prefix + getClass().getSimpleName() + "::";

	@Autowired
	TurfInterface turfInterface;
	
	@Autowired
	BookingInterface bookingInterface;
	
	@Autowired
	UserInterface userInterface;
	
	@Autowired
	AuxillaryFunctions auxillaryFunctions;
	
	public ResponseEntity<ApiResponse<List<TurfWrapper>>> getAllTurfs() {

		prefix = prefix + "getAllTurfs::";
		ApiResponse<List<TurfWrapper>> apiResponse = new ApiResponse<>();
		try {
			List<TurfWrapper> turfResponse = new ArrayList<>();
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
		} catch (FeignException e) {
			log.debug(prefix + "EXCEPTION::" + e);
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(prefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(prefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error getting all the Turfs");
		}
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}
	
	public ResponseEntity<ApiResponse<List<Slot>>> getBookedTurfSlots(Integer turfId) {

		prefix = prefix + "getBookedTurfSlots::";
		ApiResponse<List<Slot>> apiResponse = new ApiResponse<>();
		try {
			List<String> bookedSlotIds = turfInterface.getTurfById(turfId)
					.getBody()
					.getPayload()
					.getBookedSlotIds();
			List<Slot> bookedSlots = bookedSlotIds.stream()
					.map(slotId -> auxillaryFunctions.extractSlot(slotId))
					.toList();
			apiResponse.setPayload(bookedSlots);
		} catch (FeignException e) {
			log.debug(prefix + "EXCEPTION::" + e);
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(prefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(prefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error getting the booked slots");
		}
		
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<String>> addBooking(BookingDto bookingDto) {

		prefix = prefix + "addBooking::";
		ApiResponse<String> apiResponse = new ApiResponse<>();
		try {
			log.debug(prefix + bookingDto);
			List<Slot> slotList = bookingDto.getSlotList();
			List<String> slotIdList = new ArrayList<>();
 			for (Slot slot : slotList) {
				slot.setSlotId();
				log.debug(prefix + slot.getSlotId());
				slotIdList.add(slot.getSlotId());
			}
 			log.debug(prefix + slotIdList.toString());
			turfInterface.bookTurf(bookingDto.getTurfId(), slotIdList);
			log.info(prefix + "Updated Turf");
			String bookingResponse;
			try {
				bookingResponse = bookingInterface.addBooking(new Booking(bookingDto.getTurfId(), slotIdList, bookingDto.getUserId())).getBody().getPayload();
				log.info(prefix + "Updated Book");
			} catch (FeignException e) {
				log.info(prefix + "Inside Feign Exception::" + e.getClass().getSimpleName());
				if(e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
					log.error(prefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
					log.info(prefix + "Rolling back the Booking");
					turfInterface.cancelTurf(bookingDto.getTurfId(), slotIdList);
					log.info(prefix + "Rollback successful");
					throw new UnexpectedTurfBookingException(e,e.getMessage());
				}
				else {
					apiResponse = auxillaryFunctions.extractErrorResponse(e);
					log.error(prefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
					log.info(prefix + "Rolling back the Booking");
					turfInterface.cancelTurf(bookingDto.getTurfId(), slotIdList);
					log.info(prefix + "Rollback successful");
					throw new ApiResponseException(apiResponse);
				}
			} catch (Exception e) {
				log.error(prefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				log.info(prefix + "Rolling back the Booking");
				turfInterface.cancelTurf(bookingDto.getTurfId(), slotIdList);
				log.info(prefix + "Rollback successful");
				throw e;
			}
			apiResponse.setPayload(bookingResponse);

		} catch (ApiResponseException apiResponseException) {
			apiResponse = apiResponseException.getApiResponse();
			log.error(prefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw apiResponseException;
		}catch (FeignException e) {
			log.debug(prefix + "EXCEPTION::" + e);
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(prefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(prefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error while Booking");
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<BookingWrapper>> getBooking(Integer bookingId){

		prefix = prefix + "getBooking::";
		ApiResponse<BookingWrapper> apiResponse = new ApiResponse<>();
		try {
			Booking booking = bookingInterface.getBooking(bookingId).getBody().getPayload();
			Turf turf = turfInterface.getTurfById(booking.getTurfId()).getBody().getPayload();
			List<Slot> slotList = booking.getSlotIds()
					.stream()
					.map(slotId -> auxillaryFunctions.extractSlot(slotId))
					.toList();
			UserDetails user = userInterface.getUserById(booking.getUserId()).getBody().getPayload();
			BookingWrapper bookingResponse;
			bookingResponse = new BookingWrapper(bookingId,
					turf.getName(),
					turf.getAddress(),
					slotList,
					user.getFirstName() + " " + user.getLastName(),
					user.getUsername());
			apiResponse.setPayload(bookingResponse);
		} catch (FeignException e) {
			log.info(prefix + "EXCEPTION::" + e.getMessage().contains("BookingInterface"));
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(prefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(prefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error retrieving the  Booking");
		}
		
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<String>> cancelBooking(Integer bookingId) {

		prefix = prefix + "cancelBooking::";
		ApiResponse<String> apiResponse = new ApiResponse<>();
		try {
			Booking booking = bookingInterface.getBooking(bookingId).getBody().getPayload();
			turfInterface.cancelTurf(booking.getTurfId(), booking.getSlotIds());
			String bookingResponse = bookingInterface.cancelBooking(bookingId).getBody().getPayload();
			apiResponse.setPayload(bookingResponse);
		} catch (FeignException e) {
			log.debug(prefix + "EXCEPTION::" + e);
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(prefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(prefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error cancelling the Booking with the ID: " + bookingId);
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}
}
