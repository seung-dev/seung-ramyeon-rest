package app.boot.configuration.web.types;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SResponseEntity {

	public static ResponseEntity<SResponseBody> build(HttpStatus http_status, SRequestAttribute request_attribute, HttpHeaders response_header, SResponseBody response_body) {
		request_attribute.response_time(response_body.getResponse_time());
		request_attribute.error_code(response_body.getError_code());
		return ResponseEntity.status(http_status)
				.headers(response_header)
				.body(response_body)
				;
	}// end of build
	
	public static ResponseEntity<SResponseBody> build(SRequestAttribute request_attribute, HttpHeaders response_header, SResponseBody response_body) {
		return build(
				HttpStatus.OK//http_status
				, request_attribute
				, response_header
				, response_body
				);
	}// end of build
	
	public static ResponseEntity<SResponseBody> build(HttpStatus http_status, SRequestAttribute request_attribute, SResponseBody response_body) {
		request_attribute.response_time(response_body.getResponse_time());
		request_attribute.error_code(response_body.getError_code());
		return ResponseEntity.status(http_status)
				.body(response_body)
				;
	}// end of build
	
	public static ResponseEntity<SResponseBody> build(SRequestAttribute request_attribute, SResponseBody response_body) {
		return build(
				HttpStatus.OK//http_status
				, request_attribute
				, response_body
				);
	}// end of build
	
}
