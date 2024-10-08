package br.com.cnsoftware.sistema.exception;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	private ResponseEntity<String> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Duplicate entry.");
	}
	
}
