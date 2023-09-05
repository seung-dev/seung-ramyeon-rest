package app.boot.configuration.security.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.boot.configuration.security.rest.service.SSignS;
import app.boot.configuration.security.rest.service.impl.SOAuth2UserService;
import app.boot.configuration.security.rest.types.SSigninUsername;
import app.boot.configuration.security.rest.types.SSignupUsername;
import app.boot.configuration.security.rest.types.SVerifyUsername;
import app.boot.configuration.web.types.SRequestAttribute;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SSignC {

	@Resource(name = "sSignS")
	private SSignS sSignS;
	
	@Resource(name = "sOAuth2UserService")
	private SOAuth2UserService sOAuth2UserService;
	
	@RequestMapping(value = {"/rest/sign/up/username/verify"}, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verify_username(
			@RequestAttribute final SRequestAttribute request_attribute
			, @Valid @RequestBody final SVerifyUsername request_body
			) throws Exception {
		log.debug("run");
		return sSignS.verify_username(request_attribute, request_body);
	}// end of verify_username
	
	@RequestMapping(value = {"/rest/sign/up/username"}, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signup_username(
			@RequestAttribute final SRequestAttribute request_attribute
			, @Valid @RequestBody final SSignupUsername request_body
			) throws Exception {
		log.debug("run");
		return sSignS.signup_username(request_attribute, request_body);
	}// end of signup_username
	
	@RequestMapping(value = {"/rest/sign/in/username"}, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signin_username(
			@RequestAttribute final SRequestAttribute request_attribute
			, @Valid @RequestBody final SSigninUsername request_body
			) throws Exception {
		log.debug("run");
		return sSignS.signin_username(request_attribute, request_body);
	}// end of signin_username
	
	@RequestMapping(value = {"/rest/sign/in/success"}, method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signin_success(
			@RequestAttribute final SRequestAttribute request_attribute
			, final HttpServletRequest request
			) throws Exception {
		log.debug("run");
		return sSignS.signin_success(request_attribute, request);
	}// end of signin_success
	
	@RequestMapping(value = {"/rest/sign/out"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signout(
			@RequestAttribute final SRequestAttribute request_attribute
			) throws Exception {
		log.debug("run");
		return sSignS.signout(request_attribute);
	}// end of signout
	
}
