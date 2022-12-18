package com.masai.exception;

import java.time.LocalDateTime;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DepartmentException.class)
	public ResponseEntity<MyErrorDetail> departmentException(DepartmentException de, WebRequest wr){
		
		MyErrorDetail med = new MyErrorDetail();
		med.setTimestamp(LocalDateTime.now());
		med.setMessage(de.getMessage());
		med.setDescription(wr.getDescription(false));
		
		return new ResponseEntity<MyErrorDetail>(med,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyErrorDetail> invalidMethodArgumentExceptionHandler(MethodArgumentNotValidException error ){
	   
		MyErrorDetail err=new MyErrorDetail();
		err.setTimestamp(LocalDateTime.now());
		err.setMessage(" Please pass the valid argument.");
		err.setDescription(error.getBindingResult().getFieldError().getDefaultMessage());
		
		return new ResponseEntity<MyErrorDetail>(err,HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<MyErrorDetail> noHandlerExceptionHandler(NoHandlerFoundException error, WebRequest wr ){
	   
		MyErrorDetail err=new MyErrorDetail();
		err.setTimestamp(LocalDateTime.now());
		err.setMessage(" Please pass the valid argument.");
		
		
	  
	  return new ResponseEntity<MyErrorDetail>(err,HttpStatus.BAD_REQUEST);
    }
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<MyErrorDetail> otherExceptionHandler(Exception se, WebRequest req){
		
		
		MyErrorDetail err= new MyErrorDetail();
			err.setTimestamp(LocalDateTime.now());
			err.setMessage(se.getMessage());
			err.setDescription(req.getDescription(false));
				
		return new ResponseEntity<MyErrorDetail>(err, HttpStatus.BAD_REQUEST);
		
	}
	
	
	
	
}
