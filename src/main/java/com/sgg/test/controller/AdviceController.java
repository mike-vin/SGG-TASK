package com.sgg.test.controller;

import com.sgg.test.exceptions.EventNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class AdviceController {

    @ExceptionHandler({EventNotFoundException.class})
    public ResponseEntity<Object> handleEventNotFoundException(EventNotFoundException exception) {
        log.info(exception.getLocalizedMessage());
        return ResponseEntity
                .status(NOT_FOUND)
                .body(exception.getLocalizedMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleConstraintViolationException(MethodArgumentNotValidException exception) {
        log.info(exception.getLocalizedMessage());
        return ResponseEntity
                .badRequest()
                .body(exception.getLocalizedMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        log.info(exception.getLocalizedMessage());
        return ResponseEntity
                .badRequest()
                .body(exception.getLocalizedMessage());
    }
}