package app.configurations.web.types;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SResponseEntity {

	public static ResponseEntity<SResponseBody> build(SResponseBody s_response_body) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(s_response_body)
				;
	}
	
	public static ResponseEntity<SResponseBody> build(SResponseBody s_response_body, HttpHeaders response_header) {
		return ResponseEntity.status(HttpStatus.OK)
				.headers(response_header)
				.body(s_response_body)
				;
	}
	
}
