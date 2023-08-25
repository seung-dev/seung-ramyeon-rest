package app.configurations.security.filters;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import app.configurations.security.types.SCommonOAuth2Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SInMemoryClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {

	private final Map<String, ClientRegistration> registrations;
	
	public static final String _OAUTH2_CLIENT_ID = "client-id";
	
	private static final String _OAUTH2_CLIENT_SECRET = "client-secret";
	
	private static final String _OAUTH2_CLIENT_AUTHENTICATION_METHOD = "client-authentication-method";
	
	public static final String _OAUTH2_AUTHORIZATION_GRANT_TYPE = "authorization-grant-type";
	
	public static final String _OAUTH2_REDIRECT_URL = "redirect-url";
	
	public static final String _OAUTH2_SCOPE = "scope";
	
	public static final String _OAUTH2_AUTHORIZATION_URL = "authorization-url";
	
	private static final String _OAUTH2_USER_NAME_ATTRIBUTE = "user-name-attribute";
	
	private static final String _OAUTH2_CLIENT_NAME = "client-name";
	
	public SInMemoryClientRegistrationRepository(Map<String, ClientRegistration> registrations) {
		this.registrations = registrations;
	}
	
	public SInMemoryClientRegistrationRepository(Properties properties, String oauth2_providers) {
		this(init(properties, oauth2_providers));
	}
	
	private static Map<String, ClientRegistration> init(Properties properties, String providers) {
		log.debug("run");
		
		ConcurrentHashMap<String, ClientRegistration> registrations = new ConcurrentHashMap<>();
		
		for(String provider : providers.split(",")) {
			try {
				registrations.put(
						provider
						, SCommonOAuth2Provider.valueOf(provider.toUpperCase()).getBuilder(provider)
							.clientId(properties.getProperty(String.join(".", provider, _OAUTH2_CLIENT_ID)))
							.clientSecret(properties.getProperty(String.join(".", provider, _OAUTH2_CLIENT_SECRET)))
							.clientAuthenticationMethod(new ClientAuthenticationMethod(properties.getProperty(String.join(".", provider, _OAUTH2_CLIENT_AUTHENTICATION_METHOD))))
							.authorizationGrantType(new AuthorizationGrantType(properties.getProperty(String.join(".", provider, _OAUTH2_AUTHORIZATION_GRANT_TYPE))))
							.redirectUri(properties.getProperty(String.join(".", provider, _OAUTH2_REDIRECT_URL)))
							.scope(properties.getProperty(String.join(".", provider, _OAUTH2_SCOPE)).split(","))
							.authorizationUri(properties.getProperty(String.join(".", provider, _OAUTH2_AUTHORIZATION_URL)))
							.userNameAttributeName(properties.getProperty(String.join(".", provider, _OAUTH2_USER_NAME_ATTRIBUTE)))
							.clientName(properties.getProperty(String.join(".", provider, _OAUTH2_CLIENT_NAME)))
							.build()
						);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}// end of providers
		
		return Collections.unmodifiableMap(registrations);
	}// end of init
	
	@Override
	public Iterator<ClientRegistration> iterator() {
		return this.registrations.values().iterator();
	}// end of iterator
	
	@Override
	public ClientRegistration findByRegistrationId(String registrationId) {
		return this.registrations.get(registrationId);
	}// end of findByRegistrationId
	
}
