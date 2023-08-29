package app.boot.configuration.security.filter;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import app.configurations.security.filters.SJwtRequestFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private SJwtRequestFilter sJwtRequestFilter;
	
	public SSecurityConfigurerAdapter(SJwtRequestFilter sJwtRequestFilter) {
		this.sJwtRequestFilter = sJwtRequestFilter;
	}
	
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		log.debug("run");
		httpSecurity.addFilterBefore(
				this.sJwtRequestFilter//filter
				, UsernamePasswordAuthenticationFilter.class//beforeFilter
				)
		;
	}// end of configure
	
}
