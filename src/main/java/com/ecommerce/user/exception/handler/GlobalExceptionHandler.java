package com.ecommerce.user.exception.handler;

import com.ecommerce.user.payloads.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.user.exception.UserAlreadyExistException;
import com.ecommerce.user.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({UserNotFoundException.class, UserAlreadyExistException.class})
	public ResponseEntity<?> UserExceptionHandler(Exception exception){
		CommonResponse commonResponse = CommonResponse.builder().message(exception.getMessage()).httpStatus(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commonResponse);
	}
}
