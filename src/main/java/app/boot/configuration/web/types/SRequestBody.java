package app.boot.configuration.web.types;

import lombok.Getter;
import lombok.Setter;
import seung.util.kimchi.types.SType;

@Setter
@Getter
public class SRequestBody extends SType {

	private Object data;
	
}
