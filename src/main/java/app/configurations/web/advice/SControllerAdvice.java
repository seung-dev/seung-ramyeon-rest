package app.configurations.web.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import app.configurations.web.types.SResponseAdvice;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SResponseException;

@ControllerAdvice
@Slf4j
public class SControllerAdvice {

	@Value(value = "${app.header.name.error.code}")
	private String header_name_error_code;
	
	@ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<?> httpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.METHOD_NOT_ALLOWED;
		
		SResponseAdvice s_response_advice = SResponseAdvice.builder()
				.timestamp(System.currentTimeMillis())
				.status(http_status.value())
				.error(http_status.getReasonPhrase())
				.message("")
				.path(request.getRequestURI())
				.filter("SControllerAdvice")
				.build();
		
		return ResponseEntity.status(http_status).body(s_response_advice);
	}// end of httpRequestMethodNotSupportedException
	
	@ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
	public ResponseEntity<?> httpMediaTypeNotSupportedException(
			HttpMediaTypeNotSupportedException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		
		SResponseAdvice s_response_advice = SResponseAdvice.builder()
				.timestamp(System.currentTimeMillis())
				.status(http_status.value())
				.error(http_status.getReasonPhrase())
				.message("")
				.path(request.getRequestURI())
				.filter("SControllerAdvice")
				.build();
		
		return ResponseEntity.status(http_status).body(s_response_advice);
	}// end of httpRequestMethodNotSupportedException
	
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<?> methodArgumentNotValidException(
			MethodArgumentNotValidException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.BAD_REQUEST;
		
		String message = "";
		if(exception.getErrorCount() > 0) {
			FieldError field_error = (FieldError) exception.getAllErrors().get(0);
			message = field_error.getDefaultMessage();
		}
		
		SResponseAdvice s_response_advice = SResponseAdvice.builder()
				.timestamp(System.currentTimeMillis())
				.status(http_status.value())
				.error(http_status.getReasonPhrase())
				.message(message)
				.path(request.getRequestURI())
				.filter("SControllerAdvice")
				.build();
		
		return ResponseEntity.status(http_status).body(s_response_advice);
	}// end of methodArgumentNotValidException
	
	@ExceptionHandler(value = {AccessDeniedException.class})
	public ResponseEntity<?> accessDeniedException(
			AccessDeniedException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.UNAUTHORIZED;
		
		SResponseAdvice s_response_advice = SResponseAdvice.builder()
				.timestamp(System.currentTimeMillis())
				.status(http_status.value())
				.error(http_status.getReasonPhrase())
				.message("")
				.path(request.getRequestURI())
				.filter("SControllerAdvice")
				.build();
		
		return ResponseEntity.status(http_status).body(s_response_advice);
	}// end of accessDeniedException
	
	@ExceptionHandler(value = {InsufficientAuthenticationException.class})
	public ResponseEntity<?> insufficientAuthenticationException(
			InsufficientAuthenticationException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.FORBIDDEN;
		
		SResponseAdvice s_response_advice = SResponseAdvice.builder()
				.timestamp(System.currentTimeMillis())
				.status(http_status.value())
				.error(http_status.getReasonPhrase())
				.message("")
				.path(request.getRequestURI())
				.filter("SControllerAdvice")
				.build();
		
		return ResponseEntity.status(http_status).body(s_response_advice);
	}// end of insufficientAuthenticationException
	
	@ExceptionHandler(value = {BadCredentialsException.class})
	public ResponseEntity<?> badCredentialsException(
			BadCredentialsException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.UNAUTHORIZED;
		
		SResponseAdvice s_response_advice = SResponseAdvice.builder()
				.timestamp(System.currentTimeMillis())
				.status(http_status.value())
				.error(http_status.getReasonPhrase())
				.message("")
				.path(request.getRequestURI())
				.filter("SControllerAdvice")
				.build();
		
		return ResponseEntity.status(http_status).body(s_response_advice);
	}// end of badCredentialsException
	
	@ExceptionHandler(value = {DisabledException.class})
	public ResponseEntity<?> disabledException(
			DisabledException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.UNAUTHORIZED;
		
		SResponseAdvice s_response_advice = SResponseAdvice.builder()
				.timestamp(System.currentTimeMillis())
				.status(http_status.value())
				.error(http_status.getReasonPhrase())
				.message("")
				.path(request.getRequestURI())
				.filter("SControllerAdvice")
				.build();
		
		return ResponseEntity.status(http_status).body(s_response_advice);
	}// end of disabledException
	
	@ExceptionHandler(value = {ResponseStatusException.class})
	public ResponseEntity<?> responseStatusException(
			ResponseStatusException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		HttpStatus http_status = exception.getStatus();
		
		SResponseAdvice s_response_advice = SResponseAdvice.builder()
				.timestamp(System.currentTimeMillis())
				.status(http_status.value())
				.error(http_status.getReasonPhrase())
				.message("")
				.path(request.getRequestURI())
				.filter("SControllerAdvice")
				.build();
		
		return ResponseEntity.status(http_status).body(s_response_advice);
	}// end of responseStatusException
	
	@ExceptionHandler(value = {SResponseException.class})
	public ResponseEntity<?> sResponseException(
			SResponseException exception
			, HttpServletRequest request
			) {
		log.debug("run");
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(header_name_error_code, exception.error_code())
				.body(exception.s_response())
				;
	}// end of responseStatusException
	
}
