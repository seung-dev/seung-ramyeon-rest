package app.configurations.security.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.configurations.security.rest.service.SSignS;
import app.configurations.security.rest.service.impl.SOAuth2UserService;
import app.configurations.security.rest.types.SSigninUsername;
import app.configurations.security.rest.types.SSignupUsername;
import app.configurations.security.rest.types.SVerifyUsername;
import app.configurations.web.types.SRequestHeader;
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
			final SRequestHeader request_header
			, @Valid @RequestBody final SVerifyUsername request_body
			) throws Exception {
		log.debug("run");
		return sSignS.verify_username(request_header, request_body);
	}// end of verify_username
	
	@RequestMapping(value = {"/rest/sign/up/username"}, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signup_username(
			final SRequestHeader request_header
			, @Valid @RequestBody final SSignupUsername request_body
			) throws Exception {
		log.debug("run");
		return sSignS.signup_username(request_header, request_body);
	}// end of signup_username
	
	@RequestMapping(value = {"/rest/sign/in/username"}, method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signin_username(
			final SRequestHeader request_header
			, @Valid @RequestBody final SSigninUsername request_body
			) throws Exception {
		log.debug("run");
		return sSignS.signin_username(request_header, request_body);
	}// end of signin_username
	
	@RequestMapping(value = {"/rest/sign/in/success"}, method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signin_success(
			final SRequestHeader request_header
			, final HttpServletRequest request
			) throws Exception {
		log.debug("run");
		return sSignS.signin_success(request_header, request);
	}// end of signin_success
	
	@RequestMapping(value = {"/rest/sign/out"}, method = {RequestMethod.POST, RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signout(
			final SRequestHeader request_header
			) throws Exception {
		log.debug("run");
		return sSignS.signout(request_header);
	}// end of signout
	
}
