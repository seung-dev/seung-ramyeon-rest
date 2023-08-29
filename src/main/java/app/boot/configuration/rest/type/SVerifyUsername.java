package app.boot.configuration.rest.type;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import seung.util.kimchi.types.SType;

@Setter
@Getter
public class SVerifyUsername extends SType {

	@Email
	@NotBlank
	private String username;
	
}
