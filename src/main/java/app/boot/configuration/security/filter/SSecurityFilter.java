package app.boot.configuration.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import app.boot.configuration.security.rest.service.SFilterS;
import app.boot.configuration.web.types.SResponseAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SSecurityFilter extends OncePerRequestFilter {

	private SFilterS sFilterS;
	
	public SSecurityFilter(
			SFilterS sFilterS
			) {
		this.sFilterS = sFilterS;
	}
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request
			, HttpServletResponse response
			, FilterChain filterChain
			) throws ServletException, IOException {
		log.debug("run");
		
		HttpStatus http_status = HttpStatus.UNAUTHORIZED;
		
		try {
			
			Authentication authentication = null;
			
			while(true) {
				
				if(sFilterS.filter_permitall(request)) {
					break;
				}
				
				authentication = sFilterS.filter_jwt(request, response);
				if(authentication != null) {
					break;
				}
				
				authentication = sFilterS.filter_access_key(request);
				if(authentication != null) {
					break;
				}
				
				break;
			}// end of while
			
			if(authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
		} catch (Exception e) {
			http_status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		switch (http_status) {
//			case UNAUTHORIZED:
//			case FORBIDDEN:
			case INTERNAL_SERVER_ERROR:
				response.setStatus(http_status.value());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().write(
						SResponseAdvice.builder()
							.timestamp(System.currentTimeMillis())
							.status(http_status.value())
							.error(http_status.getReasonPhrase())
							.message("")
							.path(request.getRequestURI())
							.filter("SAccessKeyRequestFilter")
							.build()
							.stringify()
						);
				response.getWriter().flush();
				break;
			default:
				filterChain.doFilter(request, response);
				break;
		}// end of switch
		
	}// end of doFilterInternal
	
}
