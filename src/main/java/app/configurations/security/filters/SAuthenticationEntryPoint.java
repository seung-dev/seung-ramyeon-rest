package app.configurations.security.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import app.configurations.web.types.SResponseAdvice;
import lombok.extern.slf4j.Slf4j;

@Component(value = "sAuthenticationEntryPoint")
@Slf4j
public class SAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
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
					.message("")
					.path(request.getRequestURI())
					.filter("SAuthenticationEntryPoint")
					.build()
					.stringify()
				);
		
		response.getWriter().flush();
	}// end of commence
	
}
