package app.boot.configuration.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import app.boot.configuration.web.type.SResponseAdvice;
import lombok.extern.slf4j.Slf4j;

@Component(value = "sAccessDeniedHandler")
@Slf4j
public class SAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(
			HttpServletRequest request
			, HttpServletResponse response
			, AccessDeniedException accessDeniedException
			) throws IOException, ServletException {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.FORBIDDEN;
		
		response.setStatus(http_status.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(
				SResponseAdvice.builder()
					.timestamp(System.currentTimeMillis())
					.status(http_status.value())
					.error(http_status.getReasonPhrase())
					.message("")
					.path(request.getRequestURI())
					.filter("SAccessDeniedHandler")
					.build()
					.stringify()
				);
		
		response.getWriter().flush();
	}// end of handle
	
}
