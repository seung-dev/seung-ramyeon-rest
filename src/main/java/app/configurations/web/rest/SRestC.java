package app.configurations.web.rest;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.configurations.web.types.SRequestBody;
import app.configurations.web.types.SRequestHeader;
import app.configurations.web.types.SResponseBody;
import app.configurations.web.types.SResponseEntity;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SRestC {

	@RequestMapping(value = {"/rest/reflect/get"}, method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SResponseBody> reflect_get(
			final SRequestHeader request_header
			, @Valid final SRequestBody request_body
			) throws Exception {
		log.debug("run");
		
		SResponseBody s_response_body = SResponseBody.builder()
				.trace_id(request_header.getTrace_id())
				.build()
				;
		s_response_body.add("request_header", request_header);
		s_response_body.add("request_body", request_body);
		s_response_body.success();
		
		return SResponseEntity.build(s_response_body.done());
	}//end of reflect_get
	
	@RequestMapping(value = {"/rest/reflect/post"}, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SResponseBody> reflect_post(
			final SRequestHeader request_header
			, @Valid @RequestBody final SRequestBody request_body
			) throws Exception {
		log.debug("run");
		
		SResponseBody s_response_body = SResponseBody.builder()
				.trace_id(request_header.getTrace_id())
				.build()
				;
		s_response_body.add("request_header", request_header);
		s_response_body.add("request_body", request_body);
		s_response_body.success();
		
		return SResponseEntity.build(s_response_body.done());
	}//end of reflect_post
	
}
