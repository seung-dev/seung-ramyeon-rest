package app.configurations.web.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.SDate;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component(value = "sGateRequestFilter")
@Slf4j
public class SGateRequestFilter extends OncePerRequestFilter {

	@Value(value = "${app.header.name.trace.id}")
	private String header_name_trace_id;
	
	@Value(value = "${app.header.name.request.time}")
	private String header_name_request_time;
	
	@Value(value = "${app.header.name.error.code}")
	private String header_name_error_code;
	
//	@Resource(name = "sRestS")
//	private SRestS sRestS;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request
			, HttpServletResponse response
			, FilterChain filterChain
			) throws ServletException, IOException {
		log.debug("run");
		
		String trace_id = String.join(
				SDate.to_text("yyMMddHHmmss")//delimiter
				, RandomStringUtils.random(4, true, true)//elements
				);
		
		long request_time = System.currentTimeMillis();
		
		String uri_path = request.getRequestURI();
		log.info("({}/{}) ((BEGIN))", uri_path, trace_id);
		
		request.setAttribute(header_name_trace_id, trace_id);
		request.setAttribute(header_name_request_time, request_time);
		
//		ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
		
		try {
			
			filterChain.doFilter(request, response);
			
//			sRestS.add_rest_hist(
//					trace_id
//					, uri_path
//					, request_time
//					, System.currentTimeMillis()//time_end
//					, response.getStatus()//http_status
//					, response.getHeader(header_name_error)//error_code
//					, ""//identifier
//					, request
//					);
			
		} catch (Exception e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
		}// end of try
		
//		wrapper.copyBodyToResponse();
		
		log.info("({}/{}) ((END))", uri_path, trace_id);
	}// end of doFilterInternal
	
}
