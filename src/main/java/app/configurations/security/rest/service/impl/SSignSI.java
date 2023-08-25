package app.configurations.security.rest.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import app.configurations.datasources.SMapper0;
import app.configurations.security.crypto.SBCryptPasswordEncoder;
import app.configurations.security.jwt.SJwt;
import app.configurations.security.rest.service.SSignS;
import app.configurations.security.rest.types.SSigninUsername;
import app.configurations.security.rest.types.SSignupUsername;
import app.configurations.security.rest.types.SVerifyUsername;
import app.configurations.security.types.SCommonOAuth2Provider;
import app.configurations.security.types.SIdTokenClaimNames;
import app.configurations.security.types.SOAuth2AuthenticationException;
import app.configurations.security.types.SUser;
import app.configurations.web.types.SError;
import app.configurations.web.types.SRequestHeader;
import app.configurations.web.types.SResponseBody;
import app.configurations.web.types.SResponseEntity;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SEnv;
import seung.util.kimchi.types.SLinkedHashMap;
import seung.util.kimchi.types.SResponseException;

@Service(value = "sSignS")
@Slf4j
public class SSignSI implements SSignS {

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@Resource(name = "sEnv")
	private SEnv sEnv;
	
	@Resource(name = "sMapper0")
	private SMapper0 sMapper0;
	
	@Resource(name = "sBCryptPasswordEncoder")
	private SBCryptPasswordEncoder sBCryptPasswordEncoder;
	
	@Override
	public ResponseEntity<SResponseBody> verify_username(
			SRequestHeader request_header
			, SVerifyUsername request_body
			) throws Exception {
		log.debug("run");
		
		String uri_path = request_header.getUrl_path();
		String trace_id = request_header.getTrace_id();
		SResponseBody s_response_body = SResponseBody.builder()
				.trace_id(trace_id)
				.build()
				;
		
		int is_available = 0;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			query.merge(request_body);
			
			while(true) {
				
				SLinkedHashMap has_username = sMapper0.select_row("has_username", query);
				
				if(has_username != null) {
					s_response_body.error(SError.USERNAME_IS_NOT_AVAILABLE);
					break;
				}
				
				is_available = 1;
				
				s_response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			s_response_body.add("is_available", is_available);
		}
		
		return SResponseEntity.build(s_response_body.done());
	}// end of verify_email
	
	@Transactional
	@Override
	public ResponseEntity<SResponseBody> signup_username(
			SRequestHeader request_header
			, SSignupUsername request_body
			) throws Exception {
		log.debug("run");
		
		String uri_path = request_header.getUrl_path();
		String trace_id = request_header.getTrace_id();
		SResponseBody s_response_body = SResponseBody.builder()
				.trace_id(trace_id)
				.build()
				;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			int try_no = 0;
			int try_max = 10;
			
			while(true) {
				
				query.merge(request_body);
				
				if(sMapper0.select_row("has_username", query) != null) {
					s_response_body.error(SError.USERNAME_IS_NOT_AVAILABLE);
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
				
				s_response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
		}
		
		return SResponseEntity.build(s_response_body.done());
	}// end of signup_email
	
	@Override
	public ResponseEntity<SResponseBody> signin_username(
			SRequestHeader request_header
			, SSigninUsername request_body
			) throws Exception {
		
		String uri_path = request_header.getUrl_path();
		String trace_id = request_header.getTrace_id();
		SResponseBody s_response_body = SResponseBody.builder()
				.trace_id(trace_id)
				.build()
				;
		
		HttpHeaders response_header = new HttpHeaders();
		
		String s_access_token = "";
		String s_refresh_token = "";
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
				
				s_access_token = SJwt.generate_token(authentication);
				
				boolean is_postman = request_header.getUser_agent().toLowerCase().contains("postman");
				response_header.add(HttpHeaders.SET_COOKIE
						, SJwt.generate_access_cookie(
								s_access_token//token
								, !is_postman//http_only
								, !is_postman//secure
								)
						);
				
				s_refresh_token = SJwt.generate_token_refresh(authentication);
				response_header.add(HttpHeaders.SET_COOKIE
						, SJwt.generate_refresh_cookie(
								s_refresh_token//token
								, !is_postman//http_only
								, !is_postman//secure
								)
						);
				
				sMapper0.update("username_success", query);
				
				query.add("principal", authentication.getName());
				sMapper0.update("siginin_date", query);
				username_fail = 0;
				
				s_response_body.success();
				break;
			}// end of while
			
		} catch (BadCredentialsException e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			// 아이디, 비밀번호 검증 실패
			sMapper0.update("add_username_fail", query);
			s_response_body.add("username_fail", sMapper0.select_integer("username_fail", query));
			throw new SResponseException(s_response_body.done(SError.SIGNIN_FAILED), e);
		} catch (DisabledException e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			// 계정잠김
			throw new SResponseException(s_response_body.done(SError.SIGNIN_DISABLED), e);
		} catch (AccountExpiredException e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			// 계정만료
			throw new SResponseException(s_response_body.done(SError.SIGNIN_FAILED), e);
		} catch (CredentialsExpiredException e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			// 비밀번호만료
			throw new SResponseException(s_response_body.done(SError.PASSWORD_EXPIERED), e);
		} catch (LockedException e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			// 인증 5회 실패
			throw new SResponseException(s_response_body.done(SError.SIGNIN_LOCKED), e);
		} catch (Exception e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			s_response_body.add("username_fail", username_fail);
			s_response_body.add("s_access_token", s_access_token);
			s_response_body.add("s_refresh_token", s_refresh_token);
		}// end of try
		
		return SResponseEntity.build(s_response_body.done(), response_header);
	}// end of signin_email
	
	@Transactional
	@Override
	public ResponseEntity<SResponseBody> signin_success(
			SRequestHeader request_header
			, HttpServletRequest request
			) throws SOAuth2AuthenticationException {
		log.debug("run");
		
		String uri_path = request_header.getUrl_path();
		String trace_id = request_header.getTrace_id();
		SResponseBody s_response_body = SResponseBody.builder()
				.trace_id(trace_id)
				.build()
				;
		
		HttpHeaders response_header = new HttpHeaders();
		
		String s_access_token = "";
		String s_refresh_token = "";
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			String principal = request_header.getPrincipal();
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
			
			s_access_token = SJwt.generate_access_token(
					user_by_oauth2.get_text("principal")//subject
					, user_by_oauth2.get_text("roles")//roles
					);
			
			boolean is_postman = request_header.getUser_agent().toLowerCase().contains("postman");
			response_header.add(HttpHeaders.SET_COOKIE
					, SJwt.generate_access_cookie(
							s_access_token//token
							, !is_postman//http_only
							, !is_postman//secure
							)
					);
			
			s_refresh_token = SJwt.generate_refresh_token(
					user_by_oauth2.get_text("principal")//subject
					, user_by_oauth2.get_text("roles")//roles
					);
			response_header.add(HttpHeaders.SET_COOKIE
					, SJwt.generate_refresh_cookie(
							s_refresh_token//token
							, !is_postman//http_only
							, !is_postman//secure
							)
					);
			
			s_response_body.success();
			
		} catch (SOAuth2AuthenticationException e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			throw new SOAuth2AuthenticationException(SError.FAIL, e);
		} catch (Exception e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			s_response_body.add("s_access_token", s_access_token);
			s_response_body.add("s_refresh_token", s_refresh_token);
		}
		
		return SResponseEntity.build(s_response_body.done(), response_header);
	}// end of signin_success
	
	public SUser user_by_principal(
			String member_badge
			) {
		
		SUser s_user = null;
		
		SLinkedHashMap query = new SLinkedHashMap();
		try {
			
			query.add("member_badge", member_badge);
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
	public ResponseEntity<SResponseBody> signout(SRequestHeader request_header) throws Exception {
		
		String uri_path = request_header.getUrl_path();
		String trace_id = request_header.getTrace_id();
		SResponseBody s_response_body = SResponseBody.builder()
				.trace_id(trace_id)
				.build()
				;
		
		HttpHeaders response_header = new HttpHeaders();
		
		try {
			
			while(true) {
				
				String token = SJwt.generate_token(
						0//max_age
						, ""//subject
						, ""//roles
						);
				
				boolean is_postman = request_header.getUser_agent().toLowerCase().contains("postman");
				
				response_header.add(HttpHeaders.SET_COOKIE
						, SJwt.generate_access_cookie(
								token
								, !is_postman//http_only
								, !is_postman//secure
								)
						);
				
				response_header.add(HttpHeaders.SET_COOKIE
						, SJwt.generate_refresh_cookie(
								token
								, !is_postman//http_only
								, !is_postman//secure
								)
						);
				
				s_response_body.success();
				break;
			}// end of while
			
		} catch (Exception e) {
			log.error("({}/{}) exception=", uri_path, trace_id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}// end of try
		
		return SResponseEntity.build(s_response_body.done(), response_header);
	}// end of signout
	
}
