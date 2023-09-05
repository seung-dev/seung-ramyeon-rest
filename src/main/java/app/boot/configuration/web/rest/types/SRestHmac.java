package app.boot.configuration.web.rest.types;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SRestHmac {

	@NotBlank
	private String algorithm;
	
	@NotBlank
	private String url_path;
	
	@NotBlank
	private String access_key;
	
	@NotBlank
	private String secret_key;
	
}
