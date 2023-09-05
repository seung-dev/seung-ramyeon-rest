package app.boot.configuration.security.types;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import seung.util.kimchi.types.SType;

@Builder
@Getter
public class SOAuth2Client extends SType {

	@NotBlank
	private final String client_id;
	
	@NotBlank
	private final String authorization_grant_type;
	
	@NotBlank
	private final String redirect_url;
	
	private final String scope;
	
	@NotBlank
	private final String authorization_url;
	
}
