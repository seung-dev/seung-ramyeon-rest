package app.boot.configuration.web.types;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import seung.util.kimchi.types.SLinkedHashMap;

public class SRequest {

	public static final String _REQUEST_ATTRIBUTE_NAME = "request_attribute";
	
	public static final String[] _REMOTE_ADDR_HEADER_NAMES = {
			"x-forwarded-for"
			, "proxy-client-ip"
			, "http_x_forwarded_for"
			, "http_client_ip"
			, "wl_proxy_client_ip"
	};
	
	public static String scheme(HttpServletRequest request) {
		String scheme = request.getHeader("x-forwarded-proto");
		if(StringUtils.isNotEmpty(scheme)) {
			return scheme;
		}
		return request.getScheme();
	}
	
	public static String host(HttpServletRequest request) {
		String host = request.getHeader("host");
		if(StringUtils.isNotEmpty(host)) {
			return host;
		}
		return request.getServerName();
	}
	
	public static int port(HttpServletRequest request) {
		String port = request.getHeader("x-forwarded-port");
		if(StringUtils.isNotEmpty(port)) {
			return Integer.parseInt(port);
		}
		return request.getServerPort();
	}
	
	public static String remote_addr(HttpServletRequest request) {
		String remote_addr = "";
		for(String header_name : _REMOTE_ADDR_HEADER_NAMES) {
			remote_addr = request.getHeader(header_name);
			if(StringUtils.isEmpty(remote_addr)) {
				continue;
			}
			if("unknown".equalsIgnoreCase(remote_addr)) {
				continue;
			}
			return remote_addr;
		}
		return request.getRemoteAddr();
	}// end of remote_addr
	
	public static SLinkedHashMap headers(HttpServletRequest request) {
		SLinkedHashMap headers = new SLinkedHashMap();
		Enumeration<String> header_names = request.getHeaderNames();
		String header_name = "";
		while(header_names.hasMoreElements()) {
			header_name = header_names.nextElement();
			if(header_name == null) {
				continue;
			}
			headers.add(header_name, request.getHeader(header_name));
		}
		return headers;
	}// end of headers
	
	public static String user_agent(HttpServletRequest request) {
		return request.getHeader("user-agent");
	}// end of user_agent
	
	public static String principal(HttpServletRequest request) {
		Authentication authentication = (Authentication) request.getUserPrincipal();
		if(authentication == null) {
			return null;
		}
		return authentication.getName();
	}// end of principal
	
	public static List<String> roles(HttpServletRequest request) {
		List<String> roles = new ArrayList<String>();
		Authentication authentication = (Authentication) request.getUserPrincipal();
		if(authentication != null) {
			for(GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
				roles.add(grantedAuthority.getAuthority());
			}
		}
		return roles;
	}// end of roles
	
	public static SRequestAttribute request_attribute(HttpServletRequest request) {
		return (SRequestAttribute) request.getAttribute(_REQUEST_ATTRIBUTE_NAME);
	}// end of request_attribute
	
}
