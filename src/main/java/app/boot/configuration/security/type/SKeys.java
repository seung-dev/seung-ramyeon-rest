package app.boot.configuration.security.type;

import javax.crypto.SecretKey;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SKeys {

	private SecretKey jwt_key;
	
}
