package app.configurations.security.types;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import seung.util.kimchi.types.SError;

public class SOAuth2AuthenticationException extends OAuth2AuthenticationException {

	private static final long serialVersionUID = -468016733085704649L;
	
	public SOAuth2AuthenticationException(SError s_error, Throwable cause) {
		super(new OAuth2Error(s_error.code(), s_error.message(), null), cause);
	}
	
	public SOAuth2AuthenticationException(SError s_error) {
		super(new OAuth2Error(s_error.code(), s_error.message(), null));
	}
	
}
