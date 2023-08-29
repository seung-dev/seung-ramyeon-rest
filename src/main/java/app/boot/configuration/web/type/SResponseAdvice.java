package app.boot.configuration.web.type;

import lombok.Builder;
import lombok.Getter;
import seung.util.kimchi.types.SType;

@Builder
@Getter
public class SResponseAdvice extends SType {

	private final long timestamp;
	
	private final int status;
	
	private final String error;
	
	private final String message;
	
	private final String path;
	
	private final String filter;
	
}
