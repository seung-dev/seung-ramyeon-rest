package app.boot.configuration.web.rest;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.boot.configuration.web.rest.service.SRestS;
import app.boot.configuration.web.rest.types.SRestHmac;
import app.boot.configuration.web.types.SRequest;
import app.boot.configuration.web.types.SRequestAttribute;
import app.boot.configuration.web.types.SRequestBody;
import app.boot.configuration.web.types.SResponseBody;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SRestC {

	@Resource(name = "sRestS")
	private SRestS sRestS;
	
	@RequestMapping(value = {"/rest/reflect/get"}, method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SResponseBody> reflect_get(
			@RequestAttribute final SRequestAttribute request_attribute
			, @RequestParam(required = false) final Map<String, Object> request_param
			, HttpServletRequest request
			) throws Exception {
		log.debug("run");
		
		return sRestS.reflect_get(request_attribute, request_param, SRequest.headers(request));
	}//end of reflect_get
	
	@RequestMapping(value = {"/rest/reflect/post"}, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SResponseBody> reflect_post(
			@RequestAttribute final SRequestAttribute request_attribute
			, @Valid @RequestBody final SRequestBody request_body
			, HttpServletRequest request
			) throws Exception {
		log.debug("run");
		
		return sRestS.reflect_post(request_attribute, request_body, SRequest.headers(request));
	}//end of reflect_post
	
	@RequestMapping(value = {"/rest/reflect/hmac"}, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SResponseBody> reflect_hmac(
			@RequestAttribute final SRequestAttribute request_attribute
			, @Valid @RequestBody final SRestHmac request_body
			) throws Exception {
		log.debug("run");
		
		return sRestS.reflect_hmac(request_attribute, request_body);
	}//end of reflect_hmac
	
}
