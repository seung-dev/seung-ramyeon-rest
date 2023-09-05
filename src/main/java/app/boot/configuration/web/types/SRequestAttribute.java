package app.boot.configuration.web.types;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import seung.util.kimchi.types.SType;

@AllArgsConstructor
@Builder(builderMethodName = "defaultBuilder")
@Getter
public class SRequestAttribute extends SType {

	private String trace_id;
	
	@NotBlank
	private String local_addr;
	
	@NotBlank
	private String local_name;
	
	@NotNull
	private Integer local_port;
	
	@Builder.Default
	private long request_time = System.currentTimeMillis();
	
	private long response_time;
	
	private String http_method;
	
	private String url_scheme;
	
	private String url_domain;
	
	private int url_port;
	
	private String url_path;
	
	private String url_query;
	
	private String remote_addr;
	
	private String content_type;
	
	private String user_agent;
	
	private String referer;
	
	private String principal;
	
	private String error_code;
	
	@Builder.Default
	private ArrayList<String> roles = new ArrayList<>();
	
	public static SRequestAttributeBuilder builder(HttpServletRequest request) {
		long request_time = System.currentTimeMillis();
		return defaultBuilder()
				.trace_id(String.format("%d%s", request_time, RandomStringUtils.random(7, true, true)))
				.local_addr(request.getLocalAddr())
				.local_name(request.getLocalName())
				.local_port(request.getLocalPort())
				.http_method(request.getMethod())
				.url_scheme(SRequest.scheme(request))
				.url_domain(SRequest.host(request))
				.url_port(request.getServerPort())
				.url_path(request.getRequestURI())
				.url_query(request.getQueryString())
				.remote_addr(SRequest.remote_addr(request))
				.content_type(request.getContentType())
				.user_agent(request.getHeader("user-agent"))
				;
	}
	
	public void response_time(long response_time) {
		this.response_time = response_time;
	}
	
	public void principal(String principal) {
		this.principal = principal;
	}
	
	public SRequestAttribute role(String role) {
		this.roles.add(role);
		return this;
	}
	public void roles(ArrayList<String> roles) {
		this.roles.addAll(roles);
	}
	
	public boolean has_role(String role) {
		return this.roles.contains(role);
	}
	
	public void error_code(String error_code) {
		this.error_code = error_code;
	}
	
}
