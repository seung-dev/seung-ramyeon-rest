package app.boot.configuration.security.type;

import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

public interface SIdTokenClaimNames extends IdTokenClaimNames {

	public static final String _OAUTH2_CLAIM_SUB = "sub";
	
	public static final String _OAUTH2_CLAIM_ID = "id";
	
	public static final String _OAUTH2_CLAIM_RESPONSE = "response";
	
}
