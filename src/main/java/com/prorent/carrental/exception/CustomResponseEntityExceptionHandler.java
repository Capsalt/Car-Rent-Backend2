package com.prorent.carrental.exception;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	protected ResponseEntity<Object> handleResourceNotFoundException
	(ResourceNotFoundException ex, 
			WebRequest request) {
		
		ErrorMessage errorDetails=new ErrorMessage(new Date(),ex.getMessage(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<Object> handleBadRequestException
	(BadRequestException ex, 
			WebRequest request) {
		
		ErrorMessage errorDetails=new ErrorMessage(new Date(),ex.getMessage(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<Object> handleAccessDeniedException
	(AccessDeniedException ex, 
			WebRequest request) {
		
		ErrorMessage errorDetails=new ErrorMessage(new Date(),ex.getMessage(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.FORBIDDEN);
	}
	
	
	@ExceptionHandler(ConflictException.class)
	protected ResponseEntity<Object> handleConflictException
	(ConflictException ex, 
			WebRequest request) {
		
		ErrorMessage errorDetails=new ErrorMessage(new Date(),ex.getMessage(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UploadImageException.class)
	protected ResponseEntity<Object> handleUploadImageException
	(UploadImageException ex, 
			WebRequest request) {
		
		ErrorMessage errorDetails=new ErrorMessage(new Date(),ex.getMessage(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails,HttpStatus.EXPECTATION_FAILED);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String,Object> body=new LinkedHashMap<>();
		body.put("date",LocalDate.now());
		body.put("status",status.value());
		
		List<String> errors=ex.getBindingResult().getFieldErrors().stream()
		.map(x->x.getField()+":"+x.getDefaultMessage()).collect(Collectors.toList());
		
		body.put("errors", errors);
		return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
	}
	
}