package com.github.brunomeloesilva.os.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HandlerException extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		var problemMessage = new ProblemMessage(OffsetDateTime.now(), status.value(), ex.getMessage(), getDetailsError(ex)); 
		return handleExceptionInternal(ex, problemMessage, headers, status, request);
	}
	
	
	/* Metodos utilitarios da classe */
	
	private ArrayList<ProblemMessage.FieldDetails> getDetailsError(MethodArgumentNotValidException ex){
		var fieldDetailsError = new ArrayList<ProblemMessage.FieldDetails>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			var errorCampo = ((FieldError) error).getField();
			// var errorClasse = e.getObjectName();
			fieldDetailsError.add(new ProblemMessage.FieldDetails(errorCampo, error.getDefaultMessage()));
		}
		return fieldDetailsError;
	}
}