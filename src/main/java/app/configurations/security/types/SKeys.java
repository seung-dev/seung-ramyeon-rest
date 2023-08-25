package app.configurations.security.types;

import javax.crypto.SecretKey;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SKeys {

	private SecretKey jwt_key;
	
}
