package com.turf_booking.bookings_papi.service;


import java.util.ArrayList;
import java.util.List;
import com.turf_booking.bookings_papi.error.ApiResponseException;
import com.turf_booking.bookings_papi.error.ApiUnavailable;
import com.turf_booking.bookings_papi.error.SlotAlreadyBooked;
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

		String functionPrefix = prefix + "getAllTurfs::";
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
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			log.debug(functionPrefix + "EXCEPTION::" + e);
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error getting all the Turfs");
		}
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}
	
	public ResponseEntity<ApiResponse<List<Slot>>> getBookedTurfSlots(Integer turfId) {

		String functionPrefix = prefix + "getBookedTurfSlots::";
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
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			log.debug(functionPrefix + "EXCEPTION::" + e);
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error getting the booked slots");
		}
		
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<List<Turf>>> searchTurfBy(String parameter, String value) {

		String functionPrefix = prefix + "searchTurfBy::";
		ApiResponse<List<Turf>> apiResponse = new ApiResponse<>();
		try {
			List<Turf> turfList =  turfInterface.getTurfsByParameter(parameter,value).getBody().getPayload();
			apiResponse.setPayload(turfList);
		} catch (FeignException e) {
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error retrieving the  Booking");
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<String>> addBooking(BookingDto bookingDto) {

		String functionPrefix = prefix + "addBooking::";
		ApiResponse<String> apiResponse = new ApiResponse<>();
		try {
			log.debug(functionPrefix + bookingDto);
			List<Slot> slotList = bookingDto.getSlotList();
			List<String> slotIdList = new ArrayList<>();
 			for (Slot slot : slotList) {
				slot.setSlotId();
				log.debug(functionPrefix + slot.getSlotId());
				slotIdList.add(slot.getSlotId());
			}
 			log.debug(functionPrefix + slotIdList.toString());
			turfInterface.bookTurf(bookingDto.getTurfId(), slotIdList);
			log.info(functionPrefix + "Updated Turf");
			String bookingResponse;
			try {
				bookingResponse = bookingInterface.addBooking(new Booking(bookingDto.getTurfId(), slotIdList, bookingDto.getUserId(), bookingDto.getPrice())).getBody().getPayload();
				log.info(functionPrefix + "Updated Book");
			} catch (FeignException e) {
				log.info(functionPrefix + "Inside Feign Exception::" + e.getClass().getSimpleName());
				if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
					log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
					log.info(functionPrefix + "Rolling back the Booking");
					turfInterface.cancelTurf(bookingDto.getTurfId(), slotIdList);
					log.info(functionPrefix + "Rollback successful");
					throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
				} else {
					apiResponse = auxillaryFunctions.extractErrorResponse(e);
					log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
					log.info(functionPrefix + "Rolling back the Booking");
					turfInterface.cancelTurf(bookingDto.getTurfId(), slotIdList);
					log.info(functionPrefix + "Rollback successful");
					throw new ApiResponseException(apiResponse);
				}
			}
			apiResponse.setPayload(bookingResponse);

		} catch (ApiResponseException apiResponseException) {
			apiResponse = apiResponseException.getApiResponse();
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw apiResponseException;
		}catch (FeignException e) {
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			log.debug(functionPrefix + "EXCEPTION::" + e);
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error while Booking");
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<BookingWrapper>> getBooking(Integer bookingId){

		String functionPrefix = prefix + "getBooking::";
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
					user.getUsername(),
					booking.getPrice());
			apiResponse.setPayload(bookingResponse);
		} catch (FeignException e) {
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error retrieving the  Booking");
		}
		
		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<List<BookingWrapper>>> searchBookingsBy(String parameter, Integer value) {

		String functionPrefix = prefix + "searchBookingsBy::";
		ApiResponse<List<BookingWrapper>> apiResponse = new ApiResponse<>();
		try{
			List<Booking> bookingList = bookingInterface.getBookingBy(parameter,value).getBody().getPayload();
			List<BookingWrapper> bookingResponse = new ArrayList<>();
			bookingList.forEach(booking -> {
					 bookingResponse.add(getBooking(booking.getBookingId()).getBody().getPayload());
			});
			apiResponse.setPayload(bookingResponse);
		} catch (FeignException e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.contentUTF8().split("service ")[1]);
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error retrieving the  Booking");
		}

		return new ResponseEntity<>(apiResponse, apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<String>> cancelBooking(Integer bookingId) {

		String functionPrefix = prefix + "cancelBooking::";
		ApiResponse<String> apiResponse = new ApiResponse<>();
		try {
			Booking booking = bookingInterface.getBooking(bookingId).getBody().getPayload();
			turfInterface.cancelTurf(booking.getTurfId(), booking.getSlotIds());
			String bookingResponse;
			try {
				bookingResponse = bookingInterface.cancelBooking(bookingId).getBody().getPayload();
			} catch (FeignException e) {
				log.info(functionPrefix + "Inside Feign Exception::" + e.getClass().getSimpleName());
				if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
					log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
					log.info(functionPrefix + "Rolling back the Booking");
					turfInterface.bookTurf(booking.getTurfId(), booking.getSlotIds());
					log.info(functionPrefix + "Rollback successful");
					throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
				} else {
					apiResponse = auxillaryFunctions.extractErrorResponse(e);
					log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
					log.info(functionPrefix + "Rolling back the Booking");
					turfInterface.bookTurf(booking.getTurfId(), booking.getSlotIds());
					log.info(functionPrefix + "Rollback successful");
					throw new ApiResponseException(apiResponse);
				}
			}
			apiResponse.setPayload(bookingResponse);
		} catch (FeignException e) {
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			log.debug(functionPrefix + "EXCEPTION::" + e);
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error cancelling the Booking with the ID: " + bookingId);
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<Checkout>> getCheckout(Integer turfId, List<String> slotIds, String username) {

		String functionPrefix = prefix + "getCheckout::";
		ApiResponse<Checkout> apiResponse = new ApiResponse<>();
		try{
			Turf turf = turfInterface.getTurfById(turfId).getBody().getPayload();
			log.info(functionPrefix + "Slot IDs: " + slotIds);
			List<Slot> slotList = new ArrayList<>();
			slotIds.forEach(slotId -> {
				if(turf.getBookedSlotIds().contains(slotId))
					throw new SlotAlreadyBooked("The Slot (" + auxillaryFunctions.extractSlot(slotId).getSlotId().replace("$"," ") + ") is already booked");
				else
					slotList.add(auxillaryFunctions.extractSlot(slotId));
			});
			log.info(functionPrefix + "Slots: " + slotList);
			Integer noOfSlots = slotList.size();
			log.info(functionPrefix + "Number of Slots: " + noOfSlots);
			Integer price = turf.getPricePerHour() * noOfSlots;
			log.info(functionPrefix + "Price of the Booking: " + price);
			Checkout checkout = Checkout.builder()
					.username(username)
					.turfName(turf.getName())
					.turfAddress(turf.getAddress())
					.slots(slotList)
					.price(price)
					.build();
			apiResponse.setPayload(checkout);
		} catch (SlotAlreadyBooked e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw e;
		} catch (FeignException e) {
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error retrieving the  Booking");
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}

	public ResponseEntity<ApiResponse<String>> addUser(UserDetails userDetails) {

		String functionPrefix = prefix + "addUser::";
		ApiResponse<String> apiResponse = new ApiResponse<>();
		try{
			String userResponse = userInterface.addUser(userDetails).getBody().getPayload();
			apiResponse.setPayload(userResponse);
		} catch (FeignException e) {
			if (e.getClass().getSimpleName().contentEquals("ServiceUnavailable")) {
				log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
				throw new ApiUnavailable(e, e.contentUTF8().split("service ")[1] + " service is down");
			}
			apiResponse = auxillaryFunctions.extractErrorResponse(e);
			log.error(functionPrefix + "CAUSE::" + apiResponse.getApiError().getErrorMessage() + "::DESCRIPTION::" + apiResponse.getApiError().getCustomError());
			throw new ApiResponseException(apiResponse);
		} catch (Exception e) {
			log.error(functionPrefix + "CAUSE::" + e.getClass().getSimpleName() + "::DESCRIPTION::" + e.getMessage());
			throw new UnexpectedTurfBookingException(e, "Unexpected Error retrieving the  Booking");
		}

		return new ResponseEntity<>(apiResponse,apiResponse.getStatusMessage());
	}
}
