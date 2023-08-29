package app.configurations.security.rest.service.impl;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import app.boot.configuration.security.type.SUser;
import lombok.extern.slf4j.Slf4j;

@Service(value = "sOAuth2UserService")
@Slf4j
public class SOAuth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(
			OAuth2UserRequest userRequest
			) throws OAuth2AuthenticationException {
		log.debug("run");
		
		String registration_id = userRequest.getClientRegistration().getRegistrationId();
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		return new SUser(
				registration_id//provider
				, oAuth2User.getName()//username
				, oAuth2User.getAttributes()//attributes
				);
	}// end of loadUser
	
}
