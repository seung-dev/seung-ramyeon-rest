package app.configurations.security.rest.service.impl;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.configurations.datasources.SMapper0;
import app.configurations.security.types.SUser;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SLinkedHashMap;

@Service(value = "sUserDetailsService")
@Slf4j
public class SUserDetailsService implements UserDetailsService {

	@Resource(name = "sMapper0")
	private SMapper0 sMapper0;
	
	@Override
	public UserDetails loadUserByUsername(
			String s_username
			) throws UsernameNotFoundException {
		log.debug("run");
		
		SLinkedHashMap query = new SLinkedHashMap()
				.add("s_username", s_username)
				;
		
		SLinkedHashMap user_by_username = sMapper0.select_row("user_by_username", query);
		
		if(user_by_username == null) {
			throw new UsernameNotFoundException("UsernameNotFoundException");
		}
		
		return new SUser(
				"app"//provider
				, user_by_username.get_text("principal")//username
				, user_by_username.get_text("password")//password
				, user_by_username.get_text("roles")//roles
				, user_by_username.get_bool("enabled", false)//enabled
				, user_by_username.get_bool("account_non_expired", false)//accountNonExpired
				, user_by_username.get_bool("credentials_non_expired", false)//credentialsNonExpired
				, user_by_username.get_bool("account_non_locked", false)//accountNonLocked
				);
	}// end of loadUserByUsername
	
}
