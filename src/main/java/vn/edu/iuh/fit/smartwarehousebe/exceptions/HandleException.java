package vn.edu.iuh.fit.smartwarehousebe.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.error.ErrorResponse;

import javax.management.JMException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@Component
public class HandleException {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("Validation failed", errors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(JMException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(JMException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Validation failed", List.of("Token has expired, please log in again."));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException1(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Login failed.", List.of("UserName or password is incorrect"));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("No permission.", List.of("No permission." + ex.getMessage()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse("Token has expired.", List.of("Token has expired. Please login again."));
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapper.writeValueAsString(errorResponse));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse("Token invalid.", List.of("Token invalid . Please login again."));
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapper.writeValueAsString(errorResponse));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Not found.", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
