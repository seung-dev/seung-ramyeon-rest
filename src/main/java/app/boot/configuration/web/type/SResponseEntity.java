package app.boot.configuration.web.type;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SResponseEntity {

	public static ResponseEntity<SResponseBody> build(SResponseBody s_response_body) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(s_response_body)
				;
	}// end of build
	
	public static ResponseEntity<SResponseBody> build(SResponseBody s_response_body, HttpHeaders response_header) {
		return ResponseEntity.status(HttpStatus.OK)
				.headers(response_header)
				.body(s_response_body)
				;
	}// end of build
	
	public static ResponseEntity<SResponseBody> build(SRequestAttribute request_attribute, SResponseBody response_body) {
		request_attribute.response_time(response_body.getResponse_time());
		request_attribute.error_code(response_body.getError_code());
		return ResponseEntity.status(HttpStatus.OK)
				.body(response_body)
				;
	}// end of build
	
	public static ResponseEntity<SResponseBody> build(HttpStatus http_status, SRequestAttribute request_attribute, SResponseBody response_body) {
		request_attribute.response_time(response_body.getResponse_time());
		request_attribute.error_code(response_body.getError_code());
		return ResponseEntity.status(http_status)
				.body(response_body)
				;
	}// end of build
	
}
