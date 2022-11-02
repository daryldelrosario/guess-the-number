/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.guessnum.controller;

import com.sg.guessnum.service.InvalidGameIdException;
import com.sg.guessnum.service.InvalidGuessException;
import com.sg.guessnum.service.NoGameInDatabaseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author Daryl del Rosario
 */

@ControllerAdvice
@RestController
public class GuessNumControllerExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Error> handleMismatchException(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
             
        Error err = new Error();
        err.setTimeOfError();
        err.setMsg("gameId values MUST ONLY be INTEGERS! Please validate and try again.");
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NumberFormatException.class)
    public final ResponseEntity<Error> handleNumberFormatException(NumberFormatException ex,
            WebRequest request) {
        
        Error err = new Error();
        err.setTimeOfError();
        err.setMsg("'gameId': MUST ONLY BE INTEGERS. 'guess': WRAP YOUR 4-DIGIT GUESS AS A STRING."
                + " Please validate and try again.");
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(InvalidGameIdException.class)
    public final ResponseEntity<Error> handleInvalidGameIdException(InvalidGameIdException ex,
            WebRequest request) {
        
        Error err = new Error();
        err.setTimeOfError();
        err.setMsg(ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidGuessException.class)
    public final ResponseEntity<Error> handleInvalidGuessException(InvalidGuessException ex,
            WebRequest request) {
        
        Error err = new Error();
        err.setTimeOfError();
        err.setMsg(ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NoGameInDatabaseException.class)
    public final ResponseEntity<Error> handleNoGameInDatabaseException(NoGameInDatabaseException ex,
            WebRequest request) {
        
        Error err = new Error();
        err.setTimeOfError();
        err.setMsg(ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }
    
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, 
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        
        Error err = new Error();
        err.setTimeOfError();
        err.setMsg(ex.getMessage());
        return new ResponseEntity<>(err, status);
        
    }
    
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        
        Error err = new Error();
        err.setTimeOfError();
        err.setMsg("'gameId': MUST ONLY BE INTEGERS. 'guess': WRAP YOUR 4-DIGIT GUESS AS A STRING."
                + " Please validate and try again.");
        return new ResponseEntity<>(err, status);
    }
}
