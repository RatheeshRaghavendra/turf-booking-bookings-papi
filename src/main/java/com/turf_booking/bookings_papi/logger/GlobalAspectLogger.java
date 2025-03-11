package com.turf_booking.bookings_papi.logger;

import com.turf_booking.bookings_papi.model.*;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Log4j2
@Component
public class GlobalAspectLogger {

    String prefix = GlobalLog.prefix;

    @Pointcut("execution(* com.turf_booking.bookings_papi.*.*.*(..))")
    public void logPointCut(){}


    @Before(value = "execution(* com.turf_booking.bookings_papi.controller.*.*(..))")
    public void beforeController(JoinPoint joinPoint){
        log.info(prefix + joinPoint.getSignature().getDeclaringType().getSimpleName() + "::" + joinPoint.getSignature().getName()); ; // + "::" + joinPoint.getSignature().getName());
    }

    @Before("logPointCut()")
    public void before(JoinPoint joinPoint){
        log.debug(prefix + joinPoint.getSignature().getDeclaringType().getSimpleName() + "::" + joinPoint.getSignature().getName() + "::Called"); ; // + "::" + joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "execution(* com.turf_booking.bookings_papi.error.*.*(..))",returning = "apiResponse")
    public void afterException(JoinPoint joinPoint, ResponseEntity<ApiResponse<String>> apiResponse){
        log.error(prefix + joinPoint.getSignature().getDeclaringType().getSimpleName() + "::" + joinPoint.getSignature().getName() + "::ERROR::CODE:" + apiResponse.getBody().getStatusCode() +"::DESCRIPTION:" + apiResponse.getBody().getApiError().getErrorMessage());
    }

    @AfterReturning(value = "execution(* com.turf_booking.bookings_papi.controller.*.*(..))",returning = "apiResponse")
    public void afterListTurfWrapper(JoinPoint joinPoint, ResponseEntity<ApiResponse<List<TurfWrapper>>> apiResponse){
        log.info(prefix + joinPoint.getSignature().getDeclaringType().getSimpleName() + "::" + joinPoint.getSignature().getName() + "::Response Payload: " + apiResponse.getBody().getPayload().toString());
    }

    @AfterReturning(value = "execution(* com.turf_booking.bookings_papi.controller.*.*(..))",returning = "apiResponse")
    public void afterSlotList(JoinPoint joinPoint, ResponseEntity<ApiResponse<List<Slot>>> apiResponse){
        log.info(prefix + joinPoint.getSignature().getDeclaringType().getSimpleName() + "::" + joinPoint.getSignature().getName() + "::Response Payload: " + apiResponse.getBody().getPayload().toString());
    }

    @AfterReturning(value = "execution(* com.turf_booking.bookings_papi.controller.*.*(..))",returning = "apiResponse")
    public void afterBookingWrapper(JoinPoint joinPoint, ResponseEntity<ApiResponse<BookingWrapper>> apiResponse){
        log.info(prefix + joinPoint.getSignature().getDeclaringType().getSimpleName() + "::" + joinPoint.getSignature().getName() + "::Response Payload: " + apiResponse.getBody().getPayload().toString());
    }

    @AfterReturning(value = "execution(* com.turf_booking.bookings_papi.controller.*.*(..))",returning = "apiResponse")
    public void afterListBookingWrapper(JoinPoint joinPoint, ResponseEntity<ApiResponse<List<BookingWrapper>>> apiResponse){
        log.info(prefix + joinPoint.getSignature().getDeclaringType().getSimpleName() + "::" + joinPoint.getSignature().getName() + "::Response Payload: " + apiResponse.getBody().getPayload().toString());
    }

    @AfterReturning(value = "execution(* com.turf_booking.bookings_papi.controller.*.*(..))",returning = "apiResponse")
    public void afterString(JoinPoint joinPoint, ResponseEntity<ApiResponse<String>> apiResponse){
        log.info(prefix + joinPoint.getSignature().getDeclaringType().getSimpleName() + "::" + joinPoint.getSignature().getName() + "::Response Payload: " + apiResponse.getBody().getPayload().toString());
    }


}
