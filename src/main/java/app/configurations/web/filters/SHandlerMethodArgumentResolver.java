package app.configurations.web.filters;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import app.configurations.web.types.SRequestHeader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		log.debug("run");
		return methodParameter.getParameterType().equals(SRequestHeader.class);
	}// end of supportsParameter
	
	@Override
	public Object resolveArgument(
			MethodParameter parameter
			, ModelAndViewContainer mavContainer
			, NativeWebRequest webRequest
			, WebDataBinderFactory binderFactory
			) throws Exception {
		log.debug("run");
		return new SRequestHeader((HttpServletRequest) webRequest.getNativeRequest());
	}// end of resolveArgument
	
}
