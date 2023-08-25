package app.configurations.security.rest.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import app.configurations.security.rest.types.SSigninUsername;
import app.configurations.security.rest.types.SSignupUsername;
import app.configurations.security.rest.types.SVerifyUsername;
import app.configurations.security.types.SOAuth2AuthenticationException;
import app.configurations.web.types.SRequestHeader;
import app.configurations.web.types.SResponseBody;

public interface SSignS {

	/**
	 * <h1>Description</h1>
	 * <p>계정 중복확인</p>
	 * 
	 * @author seung
	 * @since 2023.03.05
	 */
	ResponseEntity<SResponseBody> verify_username(SRequestHeader request_header, SVerifyUsername request_body) throws Exception;
	
	/**
	 * <h1>Description</h1>
	 * <p>계정 회원가입</p>
	 * 
	 * @author seung
	 * @since 2023.03.05
	 */
	ResponseEntity<SResponseBody> signup_username(SRequestHeader request_header, SSignupUsername request_body) throws Exception;
	
	/**
	 * <h1>Description</h1>
	 * <p>계정 인증</p>
	 * 
	 * @author seung
	 * @since 2023.03.05
	 */
	ResponseEntity<SResponseBody> signin_username(SRequestHeader request_header, SSigninUsername request_body) throws Exception;
	
	/**
	 * <h1>Description</h1>
	 * <p>계정 인증 성공</p>
	 * 
	 * @author seung
	 * @since 2023.03.05
	 */
	ResponseEntity<SResponseBody> signin_success(SRequestHeader request_header, HttpServletRequest request) throws SOAuth2AuthenticationException;
	
	/**
	 * <h1>Description</h1>
	 * <p>접속종료</p>
	 * 
	 * @author seung
	 * @since 2023.03.05
	 */
	ResponseEntity<SResponseBody> signout(SRequestHeader request_header) throws Exception;
	
}
