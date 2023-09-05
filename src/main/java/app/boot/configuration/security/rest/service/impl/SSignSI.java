package app.boot.configuration.security.rest.service.impl;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import app.boot.configuration.datasource.SMapper0;
import app.boot.configuration.security.crypto.SBCryptPasswordEncoder;
import app.boot.configuration.security.jwt.SJwt;
import app.boot.configuration.security.rest.service.SSignS;
import app.boot.configuration.security.rest.types.SSigninUsername;
import app.boot.configuration.security.rest.types.SSignupUsername;
import app.boot.configuration.security.rest.types.SVerifyUsername;
import app.boot.configuration.security.types.SCommonOAuth2Provider;
import app.boot.configuration.security.types.SIdTokenClaimNames;
import app.boot.configuration.security.types.SKey;
import app.boot.configuration.security.types.SOAuth2AuthenticationException;
import app.boot.configuration.security.types.SUser;
import app.boot.configuration.types.SEnvironment;
import app.boot.configuration.web.types.SError;
import app.boot.configuration.web.types.SRequestAttribute;
import app.boot.configuration.web.types.SResponseBody;
import app.boot.configuration.web.types.SResponseEntity;
import app.boot.configuration.web.types.SResponseException;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SLinkedHashMap;

@Service(value = "sSignS")
@Slf4j
public class SSignSI implements SSignS {

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
	
	@Resource(name = "sEnvironment")
	private SEnvironment sEnvironment;
	
	@Resource(name = "sKey")
	private SKey s_key;
	
	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@Resource(name = "sBCryptPasswordEncoder")
	private SBCryptPasswordEncoder sBCryptPasswordEncoder;
	
	@Resource(name = "sMapper0")
	private SMapper0 sMapper0;
	
	@Override
	public ResponseEntity<SResponseBody> verify_username(
			SRequestAttribute request_attribute
			, SVerifyUsername request_body
			) throws Exception {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		SResponseBody response_body = SResponseBody
				.builder(request_attribute)
				.build()
				;
		
		int is_available = 0;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			query.merge(request_body);
			
			while(true) {
				
				SLinkedHashMap has_username = sMapper0.select_row("has_username", query);
				
				if(has_username != null) {
					response_body.error(SError.USERNAME_IS_NOT_AVAILABLE);
					break;
				}
				
				is_available = 1;
				
				response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			response_body.add("is_available", is_available);
		}
		
		return SResponseEntity.build(request_attribute, response_body);
	}// end of verify_email
	
	@Transactional
	@Override
	public ResponseEntity<SResponseBody> signup_username(
			SRequestAttribute request_attribute
			, SSignupUsername request_body
			) throws Exception {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		SResponseBody response_body = SResponseBody
				.builder(request_attribute)
				.build()
				;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			int try_no = 0;
			int try_max = 10;
			
			while(true) {
				
				query.merge(request_body);
				
				if(sMapper0.select_row("has_username", query) != null) {
					response_body.error(SError.USERNAME_IS_NOT_AVAILABLE);
					break;
				}
				
				String member_no = "";
				while(try_no++ < try_max) {
					query.add("member_no", "M".concat(RandomStringUtils.random(11, true, true)));
					member_no = sMapper0.select_text("member_no", query);
					if(!"".equals(member_no)) {
						break;
					}
				}// end of while
				
				String group_no = "";
				
				String group_code = request_body.getGroup_code();
				if(group_code == null || "".equals(group_code)) {
					query.add("group_code", "homeq");
					group_no = sMapper0.select_text("group_no", query);
				} else {
					group_no = sMapper0.select_text("group_no", query);
				}
				if("".equals(group_no)) {
					
				}
				
				query.add("member_no", member_no);
				query.add("group_no", group_no);
				query.add("secret", sBCryptPasswordEncoder.encode(request_body.getSecret()));
				query.add("member_role", "U");
				
				sMapper0.insert("add_username", query);
				sMapper0.insert("add_role", query);
				sMapper0.insert("add_member", query);
				
				response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
		}
		
		return SResponseEntity.build(request_attribute, response_body);
	}// end of signup_email
	
	@Override
	public ResponseEntity<SResponseBody> signin_username(
			SRequestAttribute request_attribute
			, SSigninUsername request_body
			) throws Exception {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		SResponseBody response_body = SResponseBody
				.builder(request_attribute)
				.build()
				;
		
		HttpHeaders response_header = new HttpHeaders();
		
		String s_access_token = "";
		String s_access_cookie = "";
		String s_refresh_token = "";
		String s_refresh_cookie = "";
		int username_fail = -1;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			String s_username = request_body.getUsername();
			query.add("s_username", s_username);
			
			while(true) {
				
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						s_username//principal
						, request_body.getSecret()//credentials
						);
				
				Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				s_access_token = SJwt.generate_token(
						s_key.jwt_key()//key
						, jwt_iss//iss
						, jwt_access_cookie_duration//duration
						, authentication
						);
				
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
				}// end of access cookie
				
				response_header.add(HttpHeaders.SET_COOKIE, s_access_cookie);
				
				if(jwt_refresh_cookie_duration > 0) {
					
					s_refresh_token = SJwt.generate_token(
							s_key.jwt_key()//key
							, jwt_iss//iss
							, jwt_refresh_cookie_duration//duration
							, authentication
							);
					
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
					
					response_header.add(HttpHeaders.SET_COOKIE, s_refresh_cookie);
					
				}// end of refresh cookie
				
				sMapper0.update("username_success", query);
				
				query.add("principal", authentication.getName());
				sMapper0.update("siginin_date", query);
				username_fail = 0;
				
				response_body.success();
				break;
			}// end of while
			
		} catch (BadCredentialsException e) {
			log.error("({}) exception=", trace_id, e);
			// 아이디, 비밀번호 검증 실패
			sMapper0.update("add_username_fail", query);
			response_body.add("username_fail", sMapper0.select_integer("username_fail", query));
			throw new SResponseException(response_body.done(SError.SIGNIN_FAILED), e);
		} catch (DisabledException e) {
			log.error("({}) exception=", trace_id, e);
			// 계정잠김
			throw new SResponseException(response_body.done(SError.SIGNIN_DISABLED), e);
		} catch (AccountExpiredException e) {
			log.error("({}) exception=", trace_id, e);
			// 계정만료
			throw new SResponseException(response_body.done(SError.SIGNIN_FAILED), e);
		} catch (CredentialsExpiredException e) {
			log.error("({}) exception=", trace_id, e);
			// 비밀번호만료
			throw new SResponseException(response_body.done(SError.PASSWORD_EXPIERED), e);
		} catch (LockedException e) {
			log.error("({}) exception=", trace_id, e);
			// 인증 5회 실패
			throw new SResponseException(response_body.done(SError.SIGNIN_LOCKED), e);
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			response_body.add("username_fail", username_fail);
			response_body.add("s_access_token", s_access_token);
			response_body.add("s_refresh_token", s_refresh_token);
		}// end of try
		
		return SResponseEntity.build(request_attribute, response_header, response_body);
	}// end of signin_email
	
	@Transactional
	@Override
	public ResponseEntity<SResponseBody> signin_success(
			SRequestAttribute request_attribute
			, HttpServletRequest request
			) throws SOAuth2AuthenticationException {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		SResponseBody response_body = SResponseBody
				.builder(request_attribute)
				.build()
				;
		
		HttpHeaders response_header = new HttpHeaders();
		
		String s_access_token = "";
		String s_access_cookie = "";
		String s_refresh_token = "";
		String s_refresh_cookie = "";
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			String principal = request_attribute.getPrincipal();
			SLinkedHashMap user_by_oauth2 = new SLinkedHashMap();
			
			while(true) {
				
				SLinkedHashMap oauth2 = (SLinkedHashMap) request.getAttribute("oauth2");
				
				if(oauth2 == null) {
					throw new SOAuth2AuthenticationException(SError.FAIL);
				}
				
				String provider = oauth2.get_text("provider");
				SLinkedHashMap attributes = oauth2.get_slinkedhashmap("attributes");
				
				query.add("provider", provider);
				switch(SCommonOAuth2Provider.valueOf(provider.toUpperCase())) {
					case GOOGLE:
						query.add("sub", attributes.get_text(SIdTokenClaimNames.SUB));
						query.add("email", attributes.get_text("email"));
						query.add("email_verified", attributes.get_bool("email_verified") ? "1" : "0");
						query.add("name_full", attributes.get_text("name"));
						query.add("name_nick", attributes.get_text("name"));
						query.add("avatar", attributes.get_text("picture"));
						query.add("locale", attributes.get_text("locale"));
						break;
					case FACEBOOK:
						break;
					case GITHUB:
						query.add("sub", attributes.get_text(SIdTokenClaimNames._OAUTH2_CLAIM_ID));
						query.add("email", attributes.get_text("email", ""));
						query.add("email_verified", "0");
						query.add("name_full", attributes.get_text("name"));
						query.add("name_nick", attributes.get_text("login"));
						query.add("avatar", attributes.get_text("avatar_url"));
						query.add("locale", "");
						break;
					case KAKAO:
						SLinkedHashMap kakao_account = new SLinkedHashMap(attributes.get_map("kakao_account"));
						SLinkedHashMap kakao_properties = new SLinkedHashMap(attributes.get_map("properties"));
						query.add("sub", attributes.get_text(SIdTokenClaimNames._OAUTH2_CLAIM_ID));
						query.add("email", kakao_account.get_text("email", ""));
						query.add("email_verified", kakao_account.get_bool("is_email_verified") ? "1" : "0");
						query.add("name_full", "");
						query.add("name_nick", kakao_properties.get_text("nickname"));
						query.add("avatar", kakao_properties.get_text("thumbnail_image"));
						query.add("locale", "");
						break;
					case NAVER:
						SLinkedHashMap naver_response = new SLinkedHashMap(attributes.get_map(SIdTokenClaimNames._OAUTH2_CLAIM_RESPONSE));
						query.add("sub", naver_response.get_text(SIdTokenClaimNames._OAUTH2_CLAIM_ID));
						query.add("email", naver_response.get_text("email", ""));
						query.add("email_verified", "0");
						query.add("name_full", naver_response.get_text("name"));
						query.add("name_nick", naver_response.get_text("nickname"));
						query.add("avatar", naver_response.get_text("profile_image"));
						query.add("locale", "");
						break;
					default:
						break;
				}// end of switch
				
				if(principal != null) {
					
					user_by_oauth2.merge(sMapper0.select_row("user_by_oauth2", query));
					
					if(user_by_oauth2.containsKey("principal")
							&& !principal.equals(user_by_oauth2.get_text("principal"))
							) {
						throw new SOAuth2AuthenticationException(SError.OAUTH2_IS_NOT_AVAILABLE);
					}
					
					// add
					query.add("principal", principal);
					String member_no = sMapper0.select_text("member_no_by_principal", query);
					
					query.add("member_no", member_no);
					sMapper0.insert("add_oauth2", query);
					
					break;
				}
				
				// signin
				user_by_oauth2.merge(sMapper0.select_row("user_by_oauth2", query));
				if(user_by_oauth2.containsKey("principal")) {
					sMapper0.update("update_oauth2", query);
					sMapper0.update("siginin_date", user_by_oauth2);
					break;
				}
				
				// signup
				String member_no = sMapper0.select_text("generate_member_no");
				
				query.add("member_no", member_no);
				query.add("member_role", "M");
				query.add("member_updt", member_no);
				
				sMapper0.insert("add_oauth2", query);
				sMapper0.insert("add_role", query);
				sMapper0.insert("add_member", query);
				
				user_by_oauth2.merge(sMapper0.select_row("user_by_oauth2", query));
				
				break;
			}// end of while
			
			s_access_token = SJwt.generate_token(
					s_key.jwt_key()//key
					, jwt_iss//iss
					, jwt_access_cookie_duration//duration
					, user_by_oauth2.get_text("principal")//subject
					, Collections.singletonMap(SJwt._ROLES, user_by_oauth2.get_text("roles"))//claims
					);
			
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
			
			response_header.add(HttpHeaders.SET_COOKIE, s_access_cookie);
			
			s_refresh_token = SJwt.generate_token(
					s_key.jwt_key()//key
					, jwt_iss//iss
					, jwt_refresh_cookie_duration//duration
					, user_by_oauth2.get_text("principal")//subject
					, Collections.singletonMap(SJwt._ROLES, user_by_oauth2.get_text("roles"))//claims
					);
			
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
			
			response_header.add(HttpHeaders.SET_COOKIE, s_refresh_cookie);
			
			response_body.success();
			
		} catch (SOAuth2AuthenticationException e) {
			log.error("({}) exception=", trace_id, e);
			throw new SOAuth2AuthenticationException(SError.FAIL, e);
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			response_body.add("s_access_token", s_access_token);
			response_body.add("s_refresh_token", s_refresh_token);
		}
		
		return SResponseEntity.build(request_attribute, response_header, response_body);
	}// end of signin_success
	
	public SUser user_by_principal(
			String principal
			) {
		
		SUser s_user = null;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			query.add("principal", principal);
			String roles = sMapper0.select_text("roles_by_principal", query);
			
			s_user = new SUser(
					"app"//provider
					, principal//username
					, roles
					);
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return s_user;
	}// end of user_by_principal
	
	@Override
	public ResponseEntity<SResponseBody> signout(
			SRequestAttribute request_attribute
			) throws Exception {
		log.debug("run");
		
		String trace_id = request_attribute.getTrace_id();
		
		SResponseBody response_body = SResponseBody
				.builder(request_attribute)
				.build()
				;
		
		HttpHeaders response_header = new HttpHeaders();
		
		String s_access_cookie = "";
		String s_refresh_cookie = "";
		
		try {
			
			while(true) {
				
				if(sEnvironment.is_loc()) {
					s_access_cookie = SJwt.generate_cookie_clear(
							jwt_access_cookie_name//cookie_name
							, jwt_cookie_path//cookie_path
							, SameSite.NONE//same_site
							, false//http_only
							, false//secure
							);
				} else {
					s_access_cookie = SJwt.generate_cookie_clear(
							jwt_access_cookie_name//cookie_name
							, jwt_cookie_path//cookie_path
							);
				}
				
				response_header.add(HttpHeaders.SET_COOKIE, s_access_cookie);
				
				if(sEnvironment.is_loc()) {
					s_refresh_cookie = SJwt.generate_cookie_clear(
							jwt_refresh_cookie_name//cookie_name
							, jwt_cookie_path//cookie_path
							, SameSite.NONE//same_site
							, false//http_only
							, false//secure
							);
				} else {
					s_refresh_cookie = SJwt.generate_cookie_clear(
							jwt_refresh_cookie_name//cookie_name
							, jwt_cookie_path//cookie_path
							);
				}
				
				response_header.add(HttpHeaders.SET_COOKIE, s_refresh_cookie);
				
				response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}) exception=", trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}// end of try
		
		return SResponseEntity.build(request_attribute, response_header, response_body);
	}// end of signout
	
}
