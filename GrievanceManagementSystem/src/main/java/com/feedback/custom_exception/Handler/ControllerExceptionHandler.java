package com.feedback.custom_exception.Handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.feedback.custom_exception.DepartmentAlreadyExistExcepton;
import com.feedback.custom_exception.DepartmentNotFoundException;
import com.feedback.custom_exception.TicketNotFoundException;
import com.feedback.custom_exception.UserAlreadyExistsException;
import com.feedback.custom_exception.UserNotFoundException;

/**
 * ControllerExceptionHandler class.
 * @author jagat.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    /**
     * Department not found exception handler.
     * @param ex
     * @return departmentNotFoundException
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DepartmentNotFoundException.class)
    public String departmentNotFoundException(
            final DepartmentNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * UserNotFoundException handler.
     * @param ex
     * @return userNotFoundException
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String userNotFoundException(final UserNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * TicketNotFoundException handler.
     * @param ex
     * @return TicketNotFoundException
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TicketNotFoundException.class)
    public String ticketNotFoundException(final TicketNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DepartmentAlreadyExistExcepton.class)
    public String departmentAlreadyExistsException(final DepartmentAlreadyExistExcepton ex) {
    	return ex.getMessage();
    }
    
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String userAlreadyExistsException(final UserAlreadyExistsException ex) {
    	return ex.getMessage();
    }

    /**
     * This method handles Exceptio n of MethodArgumentNotValidException.
     * @param ex
     * @return Mapping of Error and it's message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
          final MethodArgumentNotValidException ex) {
        Map<String, String> resMap = new HashMap<>();
         ex.getAllErrors().forEach(error -> {
             String fieldNameString = ((FieldError) error).getField();
             String messageString = error.getDefaultMessage();
             resMap.put(fieldNameString, messageString);
         });
        return new ResponseEntity<>(resMap, HttpStatus.BAD_REQUEST);
    }
}
