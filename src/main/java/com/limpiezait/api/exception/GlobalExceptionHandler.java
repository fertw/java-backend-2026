package com.limpiezait.api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
		return error(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
		return error(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
		return error(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
	}

	// errores de Bean Validation (@Valid): un mensaje por campo
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidacion(MethodArgumentNotValidException ex) {
		Map<String, String> errores = new LinkedHashMap<>();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			errores.put(fe.getField(), fe.getDefaultMessage());
		}
		ResponseEntity<Map<String, Object>> respuesta = error(HttpStatus.BAD_REQUEST, "Hay campos con errores");
		respuesta.getBody().put("errores", errores);
		return respuesta;
	}

	// JSON mal formado o tipo incorrecto (por ejemplo "precio": "caro")
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleJsonInvalido(HttpMessageNotReadableException ex) {
		return error(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es un JSON válido");
	}

	private ResponseEntity<Map<String, Object>> error(HttpStatus status, String mensaje) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("mensaje", mensaje);
		return ResponseEntity.status(status).body(body);
	}

}
