package app.boot.configuration.security.rest.service.impl;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import app.boot.configuration.datasource.SMapper0;
import app.boot.configuration.security.jwt.SJwt;
import app.boot.configuration.security.rest.service.SFilterS;
import app.boot.configuration.security.types.SKey;
import app.boot.configuration.security.types.SUser;
import app.boot.configuration.types.SEnvironment;
import app.boot.configuration.web.types.SError;
import app.boot.configuration.web.types.SRequest;
import app.boot.configuration.web.types.SRequestAttribute;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.SConvert;
import seung.util.kimchi.SSecurity;
import seung.util.kimchi.types.SLinkedHashMap;

@Service(value = "sFilterS")
@Slf4j
public class SFilterSI implements SFilterS {

	@Value(value = "${app.security.permitall.ant.patterns:#{null}}")
	private String permitall_ant_patterns;
	
	@Value(value = "${app.security.jwt.iss}")
	private String jwt_iss;
	
	@Value(value = "${app.security.jwt.token.type}")
	private String jwt_token_type;
	
	@Value(value = "${app.security.jwt.cookie.path}")
	private String jwt_cookie_path;
	
	@Value(value = "${app.security.jwt.access.cookie.name}")
	private String jwt_access_cookie_name;
	
	@Value(value = "${app.security.jwt.access.cookie.duration}")
	private long jwt_access_cookie_duration;
	
	@Value(value = "${app.security.jwt.refresh.cookie.name}")
	private String jwt_refresh_cookie_name;
	
	@Value(value = "${app.security.jwt.refresh.cookie.duration}")
	private long jwt_refresh_cookie_duration;
	
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
	
	@Resource(name = "sEnvironment")
	private SEnvironment sEnvironment;
	
	@Resource(name = "sKey")
	private SKey s_key;
	
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
	public Authentication filter_jwt(
			HttpServletRequest request
			, HttpServletResponse response
			) throws Exception {
		log.debug("run");
		
		SRequestAttribute request_attribute = SRequest.request_attribute(request);
		String trace_id = request_attribute.getTrace_id();
		
		Authentication authentication = null;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			SecretKey jwt_key = s_key.jwt_key();
			
			while(true) {
				
				String token = SJwt.token(
						request
						, jwt_access_cookie_name//cookie_name
						, jwt_token_type//token_type
						);
				
				if(token == null) {
					break;
				}
				
				if(SError.TOKEN_IS_VALID != SJwt.verify(jwt_key, token)) {
					if(jwt_refresh_cookie_duration > 0) {
						token = SJwt.token(request, jwt_refresh_cookie_name, jwt_token_type);
						if(SError.TOKEN_IS_VALID != SJwt.verify(jwt_key, token)) {
							break;
						}
					} else {
						break;
					}
				}
				
				authentication = SJwt.authentication(jwt_key, token);
				
				String principal = authentication.getName();
				query.add("principal", principal);
				String roles = sMapper0.select_text("roles_by_principal", query);
				
				SUser s_user = new SUser(
						"app.principal"//provider
						, principal//username
						, roles
						);
				
				authentication = new PreAuthenticatedAuthenticationToken(
						s_user//aPrincipal
						, null//aCredentials
						, s_user.getAuthorities()//anAuthorities
						);
				
				String s_access_token = SJwt.generate_token(
						s_key.jwt_key()//key
						, jwt_iss//iss
						, jwt_access_cookie_duration//duration
						, authentication
						);
				String s_access_cookie = "";
				if(sEnvironment.is_loc()) {
					s_access_cookie = SJwt.generate_cookie(
							jwt_access_cookie_name//cookie_name
							, jwt_token_type//token_type
							, s_access_token//token
							, jwt_access_cookie_duration//max_age
							, jwt_cookie_path//cookie_path
							, SameSite.NONE//same_site
							, false//http_only
							, false//secure
							);
				} else {
					s_access_cookie = SJwt.generate_cookie(
							jwt_access_cookie_name//cookie_name
							, jwt_token_type//token_type
							, s_access_token//token
							, jwt_access_cookie_duration//max_age
							, jwt_cookie_path//cookie_path
							);
				}
				response.setHeader(HttpHeaders.SET_COOKIE, s_access_cookie);
				
				if(jwt_refresh_cookie_duration > 0) {
					
					String s_refresh_token = SJwt.generate_token(
							s_key.jwt_key()//key
							, jwt_iss//iss
							, jwt_refresh_cookie_duration//duration
							, authentication
							);
					
					String s_refresh_cookie = "";
					if(sEnvironment.is_loc()) {
						s_refresh_cookie = SJwt.generate_cookie(
								jwt_refresh_cookie_name//cookie_name
								, jwt_token_type//token_type
								, s_refresh_token//token
								, jwt_refresh_cookie_duration//max_age
								, jwt_cookie_path//cookie_path
								, SameSite.NONE//same_site
								, false//http_only
								, false//secure
								);
					} else {
						s_refresh_cookie = SJwt.generate_cookie(
								jwt_refresh_cookie_name//cookie_name
								, jwt_token_type//token_type
								, s_refresh_token//token
								, jwt_refresh_cookie_duration//max_age
								, jwt_cookie_path//cookie_path
								);
					}
					
					response.setHeader(HttpHeaders.SET_COOKIE, s_refresh_cookie);
					
				}// end of refresh cookie
				
				request_attribute.principal(s_user.getName());
				request_attribute.role(s_user.roles());
				
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
		}// end of try
		
		return authentication;
	}// end of filter_jwt
	
	@Override
	public Authentication filter_access_key(
			HttpServletRequest request
			) throws Exception {
		log.debug("run");
		
		SRequestAttribute request_attribute = SRequest.request_attribute(request);
		String trace_id = request_attribute.getTrace_id();
		String url_path = request_attribute.getUrl_path();
		
		Authentication authentication = null;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
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
				
				SUser s_user = new SUser(
						"app.access.key"//provider
						, access_key//username
						, "R"//roles
						);
				
				authentication = new PreAuthenticatedAuthenticationToken(
						s_user//aPrincipal
						, null//aCredentials
						, s_user.getAuthorities()//anAuthorities
						);
				
				request_attribute.principal(s_user.getName());
				request_attribute.role(s_user.roles());
				
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
		}// end of try
		
		return authentication;
	}// end of filter_access_key
	
}
