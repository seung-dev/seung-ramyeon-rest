package app.boot.configuration.web.rest.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import app.boot.configuration.datasource.SMapper0;
import app.boot.configuration.web.rest.service.SFilterS;
import app.boot.configuration.web.type.SRequest;
import app.boot.configuration.web.type.SRequestAttribute;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.SConvert;
import seung.util.kimchi.SSecurity;
import seung.util.kimchi.types.SLinkedHashMap;

@Service(value = "sFilterS")
@Slf4j
public class SFilterSI implements SFilterS {

	@Value(value = "${app.security.permitall.ant.patterns:#{null}}")
	private String permitall_ant_patterns;
	
	@Value(value = "${app.security.hmac.algorithm:#{null}}")
	private String hmac_algorithm;
	
	@Value(value = "${app.security.hmac.timestamp.duration.ms:#{null}}")
	private long hmac_timestamp_duration_ms;
	
	@Value(value = "${app.security.hmac.timestamp.header.name:#{null}}")
	private String hmac_timestamp_header_name;
	
	@Value(value = "${app.security.hmac.access.key.header.name:#{null}}")
	private String hmac_access_key_header_name;
	
	@Value(value = "${app.security.hmac.signature.header.name:#{null}}")
	private String hmac_signature_header_name;
	
	@Resource(name = "sMapper0")
	private SMapper0 sMapper0;
	
	@Override
	public boolean filter_permitall(
			HttpServletRequest request
			) throws Exception {
		log.debug("run");
		
		boolean has_match = false;
		
		try {
			
			AntPathMatcher antPathMatcher = new AntPathMatcher();
			String request_uri = request.getRequestURI();
			
			for(String pattern : permitall_ant_patterns.split(",")) {
				if(antPathMatcher.match(pattern, request_uri)) {
					has_match = true;
					break;
				}
			}// end of match
			
		} catch (Exception e) {
			log.error("exception=", e);
		}// end of try
		
		return has_match;
	}// end of permitall
	
	@Override
	public Authentication filter_authentication(
			HttpServletRequest request
			) throws Exception {
		log.debug("run");
		
		Authentication authentication = null;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			SRequestAttribute request_attribute = SRequest.request_attribute(request);
			
			String trace_id = request_attribute.getTrace_id();
			String url_path = request_attribute.getUrl_path();
			boolean has_match = false;
			
			while(true) {
				
				// timestamp empty
				String timestamp = request.getHeader(hmac_timestamp_header_name);
				if(timestamp == null) {
					log.info("({}) Timestamp is Empty.", trace_id);
					break;
				}
				
				// timestamp duration
				if(System.currentTimeMillis() - Long.parseLong(timestamp) > hmac_timestamp_duration_ms) {
					log.info("({}) Timestamp times out.", trace_id);
					break;
				}
				
				// access key empty
				String access_key = request.getHeader(hmac_access_key_header_name);
				if(access_key == null) {
					log.info("({}) Access key is empty.", trace_id);
					break;
				}
				
				// signature empty
				String signature = request.getHeader(hmac_signature_header_name);
				if(signature == null) {
					log.info("({}) Signature is empty.", trace_id);
					break;
				}
				
				query.add("remote_addr", request.getRemoteAddr());
				query.add("access_key", access_key);
				SLinkedHashMap filter_request = sMapper0.select_row("filter_request", query);
				
				// access key not exist
				if(filter_request == null) {
					log.info("({}) Access key does not exist.", trace_id);
					break;
				}
				
				// cidr not allowed
				if(filter_request.get_int("cidr_match", 0) < 1) {
					log.info("({}) Remote address is not allowed.", trace_id);
					break;
				}
				
				// hmac
				String message = String.join(" ", url_path, timestamp, access_key);
				if(!signature.equals(
						SConvert.encode_hex(
								SSecurity.mac(
										hmac_algorithm//algorithm
										, BouncyCastleProvider.PROVIDER_NAME//provider
										, filter_request.get_text("secret_key")//key
										, message
										, StandardCharsets.UTF_8//charset
										)
								)
						)) {
					log.info("({}) Signature is not valid.", trace_id);
					break;
				}
				
				// path
				AntPathMatcher antPathMatcher = new AntPathMatcher();
				for(String pattern : filter_request.get_text("path_pattern").split(",")) {
					if(antPathMatcher.match(pattern, url_path)) {
						has_match = true;
						break;
					}
				}// end of match
				if(!has_match) {
					log.info("({}) Path is not allowed.", trace_id);
					break;
				}
				
				request_attribute.principal(access_key);
				request_attribute.role("A");
				
				authentication = new PreAuthenticatedAuthenticationToken(
						filter_request.get_text("org_no")//aPrincipal
						, null//aCredentials
						, Arrays.asList(new SimpleGrantedAuthority("A"))//anAuthorities
						);
				
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("exception=", e);
		}// end of try
		
		return authentication;
	}// end of authentication
	
	@Override
	public int add_rest_hist(
			HttpServletRequest request
			, int http_status
			) throws Exception {
		log.debug("run");
		
		int add_rest_hist = 0;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			SRequestAttribute request_attribute = SRequest.request_attribute(request);
			
			query.merge(request_attribute);
			query.add("roles", String.join(",", request_attribute.getRoles()));
			query.add("http_status", http_status);
			
			add_rest_hist = sMapper0.insert("add_rest_hist", query);
			
		} catch (Exception e) {
			log.error("exception=", e);
		}// end of try
		
		return add_rest_hist;
	}// end of add_rest_hist
	
}
