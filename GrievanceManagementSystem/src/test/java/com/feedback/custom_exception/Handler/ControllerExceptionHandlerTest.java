package com.feedback.custom_exception.Handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.feedback.custom_exception.DepartmentNotFoundException;
import com.feedback.custom_exception.TicketNotFoundException;
import com.feedback.custom_exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerTest {
    @InjectMocks
    private ControllerExceptionHandler controllerExceptionHandler;
    
    @Mock
    private MethodArgumentNotValidException ex;
    
    

    @Test
    void departmentNotFoundExceptionHandler() {
        DepartmentNotFoundException ex = new DepartmentNotFoundException();
        String result = controllerExceptionHandler
                .departmentNotFoundException(ex);
        assertEquals("Department not found", result);
    }

    @Test
    void userNotFoundExceptionHandler() {
        UserNotFoundException exception = new UserNotFoundException(
                "User not found");
        ControllerExceptionHandler controllerExceptionHandler = new ControllerExceptionHandler();
        String result = controllerExceptionHandler
                .userNotFoundException(exception);
        assertEquals("User not found with name: User not found", result);
    }

    @Test
    void ticketNotFoundExceptionHandler() {
        TicketNotFoundException ex = new TicketNotFoundException(
                "Ticket not found");
        String result = controllerExceptionHandler.ticketNotFoundException(ex);
        assertEquals("Ticket not found", result);
    }
    
    @Test
    public void testHandleMethodArgsNotValidExceptionMy() {
        FieldError fieldError1 = new FieldError("objectName", "field1", "Error message 1");
        FieldError fieldError2 = new FieldError("objectName", "field2", "Error message 2");
        List<ObjectError> fieldErrors = Arrays.asList(fieldError1, fieldError2);
        when(ex.getAllErrors()).thenReturn(fieldErrors);
        ResponseEntity<Map<String, String>> response = controllerExceptionHandler.handleMethodArgumentNotValidException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, String> responseBody = response.getBody();
        assertEquals(2, responseBody.size());
        assertEquals("Error message 1", responseBody.get("field1"));
        assertEquals("Error message 2", responseBody.get("field2"));
    }
    
   

    
   
}
