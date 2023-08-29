package app.boot.configuration.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;

import app.boot.configuration.security.type.SUser;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SLinkedHashMap;

@Slf4j
public class SAuthenticationSuccessHandler extends ForwardAuthenticationSuccessHandler {

	public SAuthenticationSuccessHandler(
			String forward_uri
			) {
		super(forward_uri);
	}
	
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request
			, HttpServletResponse response
			, Authentication authentication
			) throws IOException, ServletException {
		log.debug("run");
		
		if(authentication != null) {
			SUser s_user = (SUser) authentication.getPrincipal();
			request.setAttribute("oauth2", new SLinkedHashMap()
					.add("provider", s_user.provider())
					.add("attributes", new SLinkedHashMap(s_user.getAttributes()))
					);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}// end of onAuthenticationSuccess
	
}
