package app.boot.configuration.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;

import app.boot.configuration.web.types.SResponseAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SAuthenticationFailureHandler extends ForwardAuthenticationFailureHandler {

	public SAuthenticationFailureHandler(
			String forward_url
			) {
		super(forward_url);
	}
	
	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request
			, HttpServletResponse response
			, AuthenticationException exception
			) throws IOException, ServletException {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.UNAUTHORIZED;
		
		response.setStatus(http_status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(
				SResponseAdvice.builder()
					.timestamp(System.currentTimeMillis())
					.status(http_status.value())
					.error(http_status.getReasonPhrase())
					.message(exception.getMessage())
					.path(request.getRequestURI())
					.filter("SAuthenticationFailureHandler")
					.build()
					.stringify()
				);
		
		response.getWriter().flush();
	}// end of onAuthenticationFailure
	
}
