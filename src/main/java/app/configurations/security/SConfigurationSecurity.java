package app.configurations.security;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import app.configurations.security.crypto.SBCryptPasswordEncoder;
import app.configurations.security.filters.SAccessDeniedHandler;
import app.configurations.security.filters.SAuthenticationEntryPoint;
import app.configurations.security.filters.SAuthenticationFailureHandler;
import app.configurations.security.filters.SAuthenticationSuccessHandler;
import app.configurations.security.filters.SDaoAuthenticationProvider;
import app.configurations.security.filters.SInMemoryClientRegistrationRepository;
import app.configurations.security.filters.SJwtRequestFilter;
import app.configurations.security.jwt.SJwt;
import app.configurations.security.rest.service.impl.SOAuth2UserService;
import app.configurations.security.rest.service.impl.SUserDetailsService;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SConfigurationSecurity {

	@Value(value = "${app.security.permitall.ant.patterns}")
	private String permitall_ant_patterns;
	
	@Value(value = "${app.signin.success.url}")
	private String signin_success_url;
	
	@Value(value = "${app.signin.failure.url}")
	private String signin_failure_url;
	
	@Value(value = "${app.security.oauth2.base-url}")
	private String oauth2_base_url;
	
	@Value(value = "${app.security.oauth2.redirect-url}")
	private String oauth2_redirect_url;
	
	@Value(value = "${app.security.oauth2.providers}")
	private String oauth2_providers;
	
	
	@Value(value = "${app.security.jwt.key}")
	private String jwt_key;
	
	@Value(value = "${app.security.jwt.iss}")
	private String jwt_iss;
	
	@Value(value = "${app.security.jwt.token.type}")
	private String jwt_token_type;
	
	@Value(value = "${app.security.jwt.access.cookie.name}")
	private String jwt_access_cookie_name;
	
	@Value(value = "${app.security.jwt.refresh.cookie.name}")
	private String jwt_refresh_cookie_name;
	
	@Value(value = "${app.security.jwt.cookie.path}")
	private String jwt_cookie_path;
	
	@Value(value = "${app.security.jwt.access.cookie.duration}")
	private long jwt_access_cookie_duration;
	
	@Value(value = "${app.security.jwt.refresh.cookie.duration}")
	private long jwt_refresh_cookie_duration;
	
	
	@Resource(name = "sBCryptPasswordEncoder")
	private SBCryptPasswordEncoder sBCryptPasswordEncoder;
	
	@Resource(name = "sUserDetailsService")
	private SUserDetailsService sUserDetailsService;
	
	@Resource(name = "sOAuth2UserService")
	private SOAuth2UserService sOAuth2UserService;
	
	@ConfigurationProperties(prefix = "app.security.oauth2")
	@Bean(name = "oauth2Properties")
	public Properties oauth2Properties() {
		return new Properties();
	}
	
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationProvider authenticationProvider() {
		log.debug("run");
		
		SDaoAuthenticationProvider sDaoAuthenticationProvider = new SDaoAuthenticationProvider();
		sDaoAuthenticationProvider.setPasswordEncoder(sBCryptPasswordEncoder);
		sDaoAuthenticationProvider.setUserDetailsService(sUserDetailsService);
		
		return sDaoAuthenticationProvider;
	}// end of authenticationProvider
	
	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("");
	}// end of grantedAuthorityDefaults
	
	@Bean
	public CorsFilter corsFilter() {
		log.debug("run");
		
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/rest/**", corsConfiguration);
		
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}// end of corsFilter
	
	@Bean
	protected SecurityFilterChain securityFilterChain(
			HttpSecurity httpSecurity
			, @Qualifier(value = "oauth2Properties") Properties oauth2Properties
			) throws Exception {
		log.debug("run");
		
		// disable
		httpSecurity
			// disable
//			.anonymous().disable()
			.httpBasic().disable()
			.csrf().disable()
			.formLogin().disable()
			// filter
			.addFilterBefore(
//					new SJwtRequestFilter(ant_patterns_authorized, sJwt, sUserDetailsService)
					new SJwtRequestFilter(
							SJwt.hmac_key(jwt_key)//jwt_key
							, jwt_iss
							, jwt_token_type
							, jwt_access_cookie_name
							, jwt_refresh_cookie_name
							, jwt_cookie_path
							, jwt_access_cookie_duration
							, jwt_refresh_cookie_duration
							, sUserDetailsService
							)
					, UsernamePasswordAuthenticationFilter.class
					)
			// session
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			// exception
			.and()
				.exceptionHandling()
				.accessDeniedHandler(new SAccessDeniedHandler())
				.authenticationEntryPoint(new SAuthenticationEntryPoint())
			// requests
			.and()
				.authorizeRequests()
				.antMatchers(permitall_ant_patterns.split(",")).permitAll()
				.antMatchers("/rest/member/**").hasAnyRole("A", "M")
				.antMatchers("/rest/krx/**").hasAnyRole("A", "M", "U")
				.anyRequest().hasAnyRole("A")
//				.anyRequest().authenticated()
//			// signout
//			.and()
//				.logout()
//				.logoutUrl("/rest/sign/out")
//				.logoutSuccessHandler(new SLogoutSuccessHandler())
//			// filter and username
//			.and()
//				.apply(new SSecurityConfigurerAdapter(new SJwtRequestFilter(ant_patterns_authorized, sJwt, sUserDetailsService)))
			// oauth2
			.and()
				.oauth2Login()
				.clientRegistrationRepository(new SInMemoryClientRegistrationRepository(oauth2Properties, oauth2_providers))
				.authorizationEndpoint()
				.baseUri(oauth2_base_url)
				.and()
					.redirectionEndpoint()
					.baseUri(oauth2_redirect_url)
				.and()
					.userInfoEndpoint()
					.userService(sOAuth2UserService)
			// handler
			.and()
				.successHandler(new SAuthenticationSuccessHandler(signin_success_url))
				.failureHandler(new SAuthenticationFailureHandler(signin_failure_url))
		;
		
		return httpSecurity.build();
	}// end of securityFilterChain
	
}
