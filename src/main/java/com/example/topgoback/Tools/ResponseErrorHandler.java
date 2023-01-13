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
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<?> handleAccessDeniedException(Exception ex, WebRequest request) {
        return new ResponseEntity("Access Denied!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<?> handleParamException(MethodArgumentNotValidException ex) {
        List<String> paramErrorList = new ArrayList<>();
        for(ObjectError e:ex.getBindingResult().getAllErrors()){
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            paramErrorList.add("Field " + field + " " + message);
        }
        return new ResponseEntity(paramErrorList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<?> handleParamWrongFormatException(MethodArgumentTypeMismatchException ex)
    {
        String paramErrorList = "Field " + ex.getName() + " " + "format is not valid!";
        return new ResponseEntity(paramErrorList, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
    {
        JsonMappingException cause = (JsonMappingException) ex.getCause();
        String fieldName = cause.getPath().get(0).getFieldName();
        String paramErrorList = "Field " + fieldName + " " + "format is not valid!";
        return new ResponseEntity(paramErrorList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<?> handleBaseException(ResponseStatusException ex) {
        return new ResponseEntity(ex.getReason(), HttpStatusCode.valueOf(ex.getBody().getStatus()));
    }
}
