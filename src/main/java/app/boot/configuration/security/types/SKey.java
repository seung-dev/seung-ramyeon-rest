package app.boot.configuration.security.types;

import javax.crypto.SecretKey;

public class SKey {

	private final SecretKey jwt_key;
	
	public SKey(SecretKey jwt_key) {
		this.jwt_key = jwt_key;
	}
	
	public SKey() {
		this(null);
	}
	
	public SecretKey jwt_key() {
		return this.jwt_key;
	}
	
}
