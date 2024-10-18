package br.com.sistema.exceptions;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice	// Concentra o tratamento das Exceptions nesta classe
@RestController
public class TratamentoExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(TratamentoExceptionHandler.class);
	
	// TRATAMENTO PARA O ERRO GENÉRICO 500 - INTERNAL SERVER ERROR
	@ExceptionHandler(Exception.class)	// Define o tipo de Exceção que filtrará e entrará neste filtro
	public final ResponseEntity<ExceptionResponse> handleAllExceptions( Exception ex, WebRequest request ) {	// Método retorna um ResponseEntity do tipo ExceptionResponse(Classe que criamos)
		logger.error(null, ex);	// Loga a exceção com stack trace completo no console
		ExceptionResponse exceptionResponse = new ExceptionResponse(		// Instancia a classe modelo de resposta que criamos (ExceptionResponse)
				new Date(), 												// Define a data e horário do erro
				ex.getMessage(), 											//
				request.getDescription(false));								// 
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);	// Retonar a classe modelo preenchida e o StatusCode do erro
	}
	
	@ExceptionHandler(AutenticacaoJwtInvalidaException.class)
    public ResponseEntity<ExceptionResponse> handleAutenticacaoJwtInvalidaException(Exception ex, WebRequest request) {
		logger.error(null, ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
	
	
	@ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ExceptionResponse> handleUsuarioNaoEncontradoException(Exception ex, WebRequest request) {
        logger.error(null, ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<ExceptionResponse> handleDadosInvalidosException(Exception ex, WebRequest request) {
		logger.error(null, ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


}
