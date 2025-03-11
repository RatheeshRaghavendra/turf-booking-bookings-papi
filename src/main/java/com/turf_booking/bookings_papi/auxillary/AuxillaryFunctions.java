package com.turf_booking.bookings_papi.auxillary;

import com.google.gson.Gson;
import com.turf_booking.bookings_papi.model.ApiResponse;
import feign.FeignException;
import org.springframework.stereotype.Service;

import com.turf_booking.bookings_papi.model.Slot;

@Service
public class AuxillaryFunctions {
	
	public Slot extractSlot(String slotId) {
		
		String[] slotArray = slotId.split("\\$");
		String slotTime = slotArray[1];
		String[] date = slotArray[0].split("-");
		String day = date[0];
		String month = date[1];
		String year = date[2];
		
		return new Slot(day, month, year, slotTime);
	}

	public ApiResponse extractErrorResponse(FeignException e){
		String responseString = e.contentUTF8();
		Gson gson = new Gson();
		ApiResponse apiResponse = gson.fromJson(responseString,ApiResponse.class);
		return apiResponse;
	}

}
