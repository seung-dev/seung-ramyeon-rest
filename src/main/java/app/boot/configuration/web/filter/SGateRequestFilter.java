package app.boot.configuration.web.filter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import app.boot.configuration.web.rest.service.SRestS;
import app.boot.configuration.web.types.SRequest;
import app.boot.configuration.web.types.SRequestAttribute;
import lombok.extern.slf4j.Slf4j;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class SGateRequestFilter extends OncePerRequestFilter {

	@Resource(name = "sRestS")
	private SRestS sRestS;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request
			, HttpServletResponse response
			, FilterChain filterChain
			) throws ServletException, IOException {
		log.debug("run");
		
		SRequestAttribute request_attribute = SRequestAttribute.builder(request).build();
		String trace_id = request_attribute.getTrace_id();
		
		log.info("({}) url={}", trace_id, request.getRequestURL());
		
		request.setAttribute(SRequest._REQUEST_ATTRIBUTE_NAME, request_attribute);
		
//		ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
		
		int status = -1;
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
		} finally {
			status = response.getStatus();
		}// end of try
		
		try {
			sRestS.add_rest_hist(
					request_attribute
					, response.getStatus()//http_status
					);
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
		}// end of try
		
//		wrapper.copyBodyToResponse();
		
		log.info("({}) status={}", trace_id, status);
	}// end of doFilterInternal
	
}
