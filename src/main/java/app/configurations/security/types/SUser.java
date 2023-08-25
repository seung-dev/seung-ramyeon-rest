package app.configurations.security.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class SUser implements UserDetails, CredentialsContainer, OAuth2User {

	private static final long serialVersionUID = -8705040312331671222L;
	
	private final String provider;
	
	private final String username;
	
	private String password;
	
	private final Collection<? extends GrantedAuthority> authorities;
	
	private final Map<String, Object> attributes;
	
	private final boolean accountNonExpired;
	
	private final boolean accountNonLocked;
	
	private final boolean credentialsNonExpired;
	
	private final boolean enabled;
	
	public SUser(
			String provider
			, String username
			, String password
			, Collection<? extends GrantedAuthority> authorities
			, Map<String, Object> attributes
			, boolean accountNonExpired
			, boolean accountNonLocked
			, boolean credentialsNonExpired
			, boolean enabled
			) {
		this.provider = provider;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.authorities = Collections.unmodifiableCollection(authorities);
		this.attributes = attributes;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
	}
	
	public SUser(
			String provider
			, String username
			, String password
			, String roles
			, boolean accountNonExpired
			, boolean accountNonLocked
			, boolean credentialsNonExpired
			, boolean enabled
			) {
		this(provider, username, password, authorities(roles), null, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
	}
	
	public SUser(
			String provider
			, String username
			, String roles
			) {
		this(provider, username, null, authorities(roles), null, true, true, true, true);
	}
	
	public SUser(
			String provider
			, String username
			, Map<String, Object> attributes
			) {
		this(provider, username, null, new ArrayList<>(), attributes, true, true, true, true);
	}
	
	public String provider() {
		return this.provider;
	}
	
	private static List<GrantedAuthority> authorities(String roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles.split(",")) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	
	public String roles() {
		StringBuffer roles = new StringBuffer();
		for(GrantedAuthority grantedAuthority : getAuthorities()) {
			roles.append(",");
			roles.append(grantedAuthority.getAuthority());
		}
		return roles.substring(1);
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public void eraseCredentials() {
		this.password = null;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}// end of getAuthorities
	
	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}// end of isAccountNonExpired
	
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}// end of isAccountNonLocked
	
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}// end of isCredentialsNonExpired
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}// end of isEnabled
	
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}// end of getAttributes
	
	@Override
	public String getName() {
		return this.username;
	}// end of getName
	
}
