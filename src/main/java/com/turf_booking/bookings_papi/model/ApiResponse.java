package com.turf_booking.bookings_papi.model;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;


public class ApiResponse<T> {
	
	Integer statusCode;
	HttpStatus statusMessage;
	@Nullable
	T payload;
	@Nullable
	ApiError apiError;
	

	public ApiResponse() {
		super();
		this.statusCode = 200;
		this.statusMessage = HttpStatus.valueOf(statusCode);
	}

	public ApiResponse(Integer statusCode, T payload, ApiError apiError) {
		super();
		this.statusCode = statusCode;
		this.statusMessage = HttpStatus.valueOf(statusCode);
		this.payload = payload;
		this.apiError = apiError;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusMessage = HttpStatus.valueOf(statusCode);
		this.statusCode = statusCode;
	}

	public HttpStatus getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(HttpStatus statusMessage) {
		this.statusMessage = statusMessage;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	public ApiError getApiError() {
		return apiError;
	}

	public void setApiError(ApiError apiError) {
		this.apiError = apiError;
	}
	
	
	
	
	
	
	
	
	

}
