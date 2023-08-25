package app.configurations.security.filters;

import java.io.IOException;
import java.security.Key;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import app.configurations.security.jwt.SJwt;
import app.configurations.security.rest.service.impl.SUserDetailsService;
import app.configurations.security.types.SUser;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SError;

@Slf4j
public class SJwtRequestFilter extends OncePerRequestFilter {

	private final SecretKey jwt_key;
	
	private final String jwt_iss;
	
	private final String jwt_token_type;
	
	private final String jwt_access_cookie_name;
	
	private final String jwt_refresh_cookie_name;
	
	private final String jwt_cookie_path;
	
	private final long jwt_access_cookie_duration;
	
	private final long jwt_refresh_cookie_duration;
	
	private final SUserDetailsService sUserDetailsService;
	
	public SJwtRequestFilter(
			SecretKey jwt_key
			, String jwt_iss
			, String jwt_token_type
			, String jwt_access_cookie_name
			, String jwt_refresh_cookie_name
			, String jwt_cookie_path
			, long jwt_access_cookie_duration
			, long jwt_refresh_cookie_duration
			, SUserDetailsService sUserDetailsService
			) {
		this.jwt_key = jwt_key;
		this.jwt_iss = jwt_iss;
		this.jwt_token_type = jwt_token_type;
		this.jwt_access_cookie_name = jwt_access_cookie_name;
		this.jwt_refresh_cookie_name = jwt_refresh_cookie_name;
		this.jwt_cookie_path = jwt_cookie_path;
		this.jwt_access_cookie_duration = jwt_access_cookie_duration;
		this.jwt_refresh_cookie_duration = jwt_refresh_cookie_duration;
		this.sUserDetailsService = sUserDetailsService;
	}// end of SJwtRequestFilter
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request
			, HttpServletResponse response
			, FilterChain filterChain
			) throws ServletException, IOException {
		log.debug("run");
		
		try {
			
			while(true) {
				
				String token = SJwt.token(request, jwt_access_cookie_name, jwt_token_type);
				
				// token validation
				if(token == null) {
					break;
				}
				if(SError.TOKEN_IS_VALID != SJwt.verify(jwt_key, token)) {
					if(jwt_access_cookie_duration > 0) {
						token = SJwt.token(request, jwt_refresh_cookie_name, jwt_token_type);
						if(SError.TOKEN_IS_VALID != SJwt.verify(jwt_key, token)) {
							break;
						}
					}
					break;
				}
				
				Authentication authentication = SJwt.authentication(jwt_key, token);
				
				String principal = authentication.getName();
				String roles = sUserDetailsService.roles_by_principal(principal);
				
				String token_access_post = SJwt.generate_token(
						jwt_key//key
						, jwt_iss//iss
						, jwt_access_cookie_duration//duration
						, authentication
						);
				
				response.setHeader(
						HttpHeaders.SET_COOKIE
						, SJwt.generate_cookie(
								jwt_access_cookie_name//cookie_name
								, jwt_token_type//token_type
								, token_access_post//token
								, jwt_access_cookie_duration//max_age
								, jwt_cookie_path//cookie_path
								, SameSite.LAX//same_site
								, true//http_only
								, true//secure
								)
						);
				
				if(jwt_access_cookie_duration > 0) {
					
					String token_refresh_post = SJwt.generate_token(
							jwt_key//key
							, jwt_iss//iss
							, jwt_access_cookie_duration//duration
							, authentication
							);
					
					response.setHeader(
							HttpHeaders.SET_COOKIE
							, SJwt.generate_cookie(
									jwt_refresh_cookie_name//cookie_name
									, jwt_token_type//token_type
									, token_refresh_post//token
									, jwt_refresh_cookie_duration//max_age
									, jwt_cookie_path//cookie_path
									, SameSite.LAX//same_site
									, true//http_only
									, true//secure
									)
							);
					
				}// end of refresh token
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				break;
			}// end of while
			
			filterChain.doFilter(request, response);
			
		} catch (Exception e) {
			log.error("exception=", e);
		}
		
	}// end of doFilterInternal
	
}
