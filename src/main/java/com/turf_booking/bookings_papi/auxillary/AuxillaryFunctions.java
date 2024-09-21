package com.turf_booking.bookings_papi.auxillary;

import org.springframework.stereotype.Service;

import com.turf_booking.bookings_papi.model.Slot;

@Service
public class AuxillaryFunctions {
	
	public Slot extractSlot(String slotId) {
		
		String[] slotArray = slotId.split("\\$");
//		System.out.println("slot array : " + slotArray[0]);
		String slotTime = slotArray[1];
		String[] date = slotArray[0].split("-");
//		System.out.println("Date format: " + date);
		String day = date[0];
		String month = date[1];
		String year = date[2];
		
		return new Slot(day, month, year, slotTime);
		
	}

}
