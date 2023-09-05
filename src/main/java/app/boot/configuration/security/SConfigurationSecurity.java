package app.boot.configuration.security;

import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.codec.DecoderException;
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

import app.boot.configuration.security.crypto.SBCryptPasswordEncoder;
import app.boot.configuration.security.filter.SAccessDeniedHandler;
import app.boot.configuration.security.filter.SAuthenticationEntryPoint;
import app.boot.configuration.security.filter.SAuthenticationFailureHandler;
import app.boot.configuration.security.filter.SAuthenticationSuccessHandler;
import app.boot.configuration.security.filter.SDaoAuthenticationProvider;
import app.boot.configuration.security.filter.SInMemoryClientRegistrationRepository;
import app.boot.configuration.security.filter.SSecurityFilter;
import app.boot.configuration.security.jwt.SJwt;
import app.boot.configuration.security.rest.service.SFilterS;
import app.boot.configuration.security.rest.service.impl.SOAuth2UserService;
import app.boot.configuration.security.rest.service.impl.SUserDetailsService;
import app.boot.configuration.security.types.SKey;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SConfigurationSecurity {

	@Value(value = "${app.security.permitall.ant.patterns}")
	private String permitall_ant_patterns;
	
	@Resource(name = "sFilterS")
	private SFilterS sFilterS;
	
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
	
	@Resource(name = "sBCryptPasswordEncoder")
	private SBCryptPasswordEncoder sBCryptPasswordEncoder;
	
	@Resource(name = "sUserDetailsService")
	private SUserDetailsService sUserDetailsService;
	
	@Resource(name = "sOAuth2UserService")
	private SOAuth2UserService sOAuth2UserService;
	
	@Bean(name = "sKey")
	public SKey sKey() throws WeakKeyException, DecoderException {
		return new SKey(SJwt.hmac_key(jwt_key));
	}// end of sEnvironment
	
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
		
//		corsConfiguration.setAllowedOriginPatterns(
//				Arrays.asList(
//						"https://www.restful.kr"
//						, "https://10.0.1.100:10605"
//						, "http://192.168.*:[*]"
//						, "http://127.0.0.1:[*]"
//						, "http://localhost:[*]"
//						, "file://index.html"
//						, "null"
//						, "-"
//				)
//		);
//		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST"));
//		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Accept", "Content-Type", "X-Requested-With"));
		
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
					new SSecurityFilter(sFilterS)
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
