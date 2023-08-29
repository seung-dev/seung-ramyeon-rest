package app.boot.configuration.web.filter;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SLinkedHashMap;

@Deprecated
@Slf4j
public class SHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.debug("run");
		return parameter.getParameterType().equals(SLinkedHashMap.class);
	}// end of supportsParameter
	
	@Override
	public Object resolveArgument(
			MethodParameter parameter
			, ModelAndViewContainer mavContainer
			, NativeWebRequest webRequest
			, WebDataBinderFactory binderFactory
			) throws Exception {
		log.debug("run");
		
		SLinkedHashMap request_param = new SLinkedHashMap();
		
		if(parameter.getParameterType().equals(SLinkedHashMap.class)) {
			
			HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
			
			Enumeration<?> enumerations = httpServletRequest.getParameterNames();
			
			Object element = null;
			String key = null;
			while(enumerations.hasMoreElements()) {
				
				element = enumerations.nextElement();
				key = element == null ? "" : (String) element;
				
				request_param.add(key, httpServletRequest.getParameter(key));
				
			}// end of while
			
		}// end of parameters
		
		return request_param;
	}// end of resolveArgument
	
}
