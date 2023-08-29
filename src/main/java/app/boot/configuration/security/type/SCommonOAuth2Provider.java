package app.boot.configuration.security.type;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * Common OAuth2 Providers that can be used to create
 * {@link org.springframework.security.oauth2.client.registration.ClientRegistration.Builder
 * builders} pre-configured with sensible defaults.
 *
 * @author Phillip Webb
 * @since 5.0
 */
public enum SCommonOAuth2Provider {

	GOOGLE {
		
		@Override
		public Builder getBuilder(String registration_id) {
			ClientRegistration.Builder builder = getBuilder(registration_id, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			builder.scope("openid", "profile", "email");
			builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
			builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
			builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
			builder.issuerUri("https://accounts.google.com");
			builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
			builder.userNameAttributeName(SIdTokenClaimNames._OAUTH2_CLAIM_SUB);
			builder.clientName("Google");
			return builder;
		}
		
	}
	, APPLE {
		
		@Override
		public Builder getBuilder(String registration_id) {
			ClientRegistration.Builder builder = getBuilder(registration_id, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			return builder;
		}
		
	}
	, FACEBOOK {
		
		@Override
		public Builder getBuilder(String registration_id) {
			ClientRegistration.Builder builder = getBuilder(registration_id, ClientAuthenticationMethod.CLIENT_SECRET_POST, DEFAULT_REDIRECT_URL);
			builder.authorizationUri("https://www.facebook.com/v2.8/dialog/oauth");
			builder.tokenUri("https://graph.facebook.com/v2.8/oauth/access_token");
			builder.userInfoUri("https://graph.facebook.com/me?fields=id,name,email");
			builder.userNameAttributeName(SIdTokenClaimNames._OAUTH2_CLAIM_ID);
			builder.clientName("Facebook");
			return builder;
		}
		
	}
	, GITHUB {
		
		@Override
		public Builder getBuilder(String registration_id) {
			ClientRegistration.Builder builder = getBuilder(registration_id, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			builder.scope("read:user");
			builder.authorizationUri("https://github.com/login/oauth/authorize");
			builder.tokenUri("https://github.com/login/oauth/access_token");
			builder.userInfoUri("https://api.github.com/user");
			builder.userNameAttributeName(SIdTokenClaimNames._OAUTH2_CLAIM_ID);
			builder.clientName("GitHub");
			return builder;
		}
		
	}
	, OKTA {
		
		@Override
		public Builder getBuilder(String registration_id) {
			ClientRegistration.Builder builder = getBuilder(registration_id, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			builder.userNameAttributeName(SIdTokenClaimNames._OAUTH2_CLAIM_SUB);
			builder.clientName("Okta");
			return builder;
		}
		
	}
	, KAKAO {
		
		@Override
		public Builder getBuilder(String registration_id) {
			ClientRegistration.Builder builder = getBuilder(registration_id, ClientAuthenticationMethod.CLIENT_SECRET_POST, DEFAULT_REDIRECT_URL);
			builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
			builder.authorizationUri("https://kauth.kakao.com/oauth/authorize");
			builder.tokenUri("https://kauth.kakao.com/oauth/token");
			builder.userInfoUri("https://kapi.kakao.com/v2/user/me");
			builder.userNameAttributeName(SIdTokenClaimNames._OAUTH2_CLAIM_ID);
			builder.clientName("Kakao");
			return builder;
		}
		
	}
	, NAVER {
		
		@Override
		public Builder getBuilder(String registration_id) {
			ClientRegistration.Builder builder = getBuilder(registration_id, ClientAuthenticationMethod.CLIENT_SECRET_BASIC, DEFAULT_REDIRECT_URL);
			builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
			builder.authorizationUri("https://nid.naver.com/oauth2.0/authorize");
			builder.tokenUri("https://nid.naver.com/oauth2.0/token");
			builder.userInfoUri("https://openapi.naver.com/v1/nid/me");
			builder.userNameAttributeName(SIdTokenClaimNames._OAUTH2_CLAIM_RESPONSE);
			builder.clientName("Naver");
			return builder;
		}
		
	}
	;
	
	private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";
	
	protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method, String redirectUri) {
		ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
		builder.clientAuthenticationMethod(method);
		builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
		builder.redirectUri(redirectUri);
		return builder;
	}
	
	/**
	 * Create a new
	 * {@link org.springframework.security.oauth2.client.registration.ClientRegistration.Builder
	 * ClientRegistration.Builder} pre-configured with provider defaults.
	 * @param registrationId the registration-id used with the new builder
	 * @return a builder instance
	 */
	public abstract ClientRegistration.Builder getBuilder(String registration_id);
	
}
