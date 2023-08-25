package app.configurations.web.rest.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.configurations.web.rest.service.SRestS;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SLinkedHashMap;

@Service(value = "sRestS")
@Slf4j
public class SRestSI implements SRestS {

//	@Resource(name = "sMapper0")
//	private SMapper0 sMapper0;
//	
//	@Override
//	public void add_rest_hist(
//			String uri_path
//			, String trace_id
//			, long time_begin
//			, long time_end
//			, int http_status
//			, String error_code
//			, String identifier
//			, HttpServletRequest request
//			) throws Exception {
//		
//		SLinkedHashMap query = new SLinkedHashMap();
//		try {
//			
//			query.add("uri_path", uri_path);
//			query.add("trace_id", trace_id);
//			query.add("time_begin", time_begin);
//			query.add("time_end", time_end);
//			query.add("http_status", http_status);
//			query.add("error_code", error_code);
//			query.add("identifier", identifier);
//			query.add("remote_addr", request.getRemoteAddr());
//			query.add("http_method", request.getMethod());
//			query.add("uri_scheme", request.getScheme());
//			query.add("uri_host", request.getHeader("Host"));
//			query.add("content_type", request.getContentType());
//			query.add("user_agent", request.getHeader("User-Agent"));
//			query.add("content_length", request.getContentLengthLong());
//			
//			sMapper0.insert("add_rest_hist", query);
//			
//		} catch (Exception e) {
//			log.error("exception=", e);
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
//		}// end of try
//		
//	}// end of add_rest_hist
	
}
