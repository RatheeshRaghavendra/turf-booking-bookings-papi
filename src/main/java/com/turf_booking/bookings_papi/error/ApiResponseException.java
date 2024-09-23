package com.turf_booking.bookings_papi.error;

import com.turf_booking.bookings_papi.model.ApiError;
import com.turf_booking.bookings_papi.model.ApiResponse;
import lombok.Data;

@Data
public class ApiResponseException extends RuntimeException {

    ApiResponse apiResponse;

    public ApiResponseException(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }
}
