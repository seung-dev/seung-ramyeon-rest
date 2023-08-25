package app.configurations.web.types;

import java.security.Principal;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import seung.util.kimchi.types.SLinkedHashMap;
import seung.util.kimchi.types.SType;

@AllArgsConstructor
@Getter
public class SRequestHeader extends SType {

	@NotBlank
	private final String trace_id;
	
	@NotBlank
	private final String url_path;
	
	@NotBlank
	private final String remote_addr;
	
	private final String user_agent;
	
	private final String x_forwarded_for;
	
	private final String principal;
	
	private final SLinkedHashMap headers;
	
	public SRequestHeader(HttpServletRequest request) {
		this(trace_id(request)//trace_id
				, request.getRequestURI()//url_path
				, request.getRemoteAddr()//remote_addr
				, x_forwarded_for(request)
				, request.getHeader("user-agent")//user_agent
				, principal(request)
				, headers(request)
				);
	}
	
	private static String trace_id(HttpServletRequest request) {
		Object attribute = request.getAttribute("trace_id");
		if(attribute != null) {
			return attribute.toString();
		}
		return null;
	}
	
	private static String x_forwarded_for(HttpServletRequest request) {
		String header = request.getHeader("X-Forwarded-For");
		if(header != null) {
			return header;
		}
		header = request.getHeader("Proxy-Client-IP");
		if(header != null) {
			return header;
		}
		header = request.getHeader("WL-Proxy-Client-IP");
		if(header != null) {
			return header;
		}
		header = request.getHeader("HTTP_CLIENT_IP");
		if(header != null) {
			return header;
		}
		return request.getHeader("HTTP_X_FORWARDED_FOR");
	}
	
	private static String principal(HttpServletRequest request) {
		Principal user_principal = request.getUserPrincipal();
		if(user_principal != null) {
			return user_principal.getName();
		}
		return null;
	}
	
	private static SLinkedHashMap headers(HttpServletRequest request) {
		SLinkedHashMap headers = new SLinkedHashMap();
		Enumeration<String> header_names = request.getHeaderNames();
		String header_name = "";
		while(header_names.hasMoreElements()) {
			header_name = header_names.nextElement();
			if(header_name == null
					|| "user-agent".equalsIgnoreCase(header_name)
					|| "x-forwarded-for".equalsIgnoreCase(header_name)
					) {
				continue;
			}
			headers.add(header_name, request.getHeader(header_name));
		}
		return headers;
	}
	
}
