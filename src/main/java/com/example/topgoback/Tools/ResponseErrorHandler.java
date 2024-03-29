package com.example.topgoback.Tools;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
public class ResponseErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<?> handleParamException(MethodArgumentNotValidException ex) {
        List<String> paramErrorList = new ArrayList<>();
        for(ObjectError e:ex.getBindingResult().getAllErrors()){
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            paramErrorList.add("Field " + field + " " + message);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, paramErrorList.toString());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<?> handleParamWrongFormatException(MethodArgumentTypeMismatchException ex)
    {
        String paramErrorList = "Field " + ex.getName() + " " + "format is not valid!";
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, paramErrorList.toString());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
    {
        JsonMappingException cause = (JsonMappingException) ex.getCause();
        String fieldName = cause.getPath().get(0).getFieldName();
        String paramErrorList = "Field " + fieldName + " " + "format is not valid!";
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, paramErrorList.toString());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public final ResponseEntity<?> handleHeaderException(MissingRequestHeaderException ex) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,ex.getBody().getDetail());
    }
}
