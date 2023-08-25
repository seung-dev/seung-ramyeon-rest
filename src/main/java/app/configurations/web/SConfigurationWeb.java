package app.configurations.web;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import app.configurations.web.filters.SHandlerMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SConfigurationWeb extends WebMvcConfigurationSupport {

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		log.debug("run");
		argumentResolvers.add(new SHandlerMethodArgumentResolver());
	}// end of addArgumentResolvers
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.debug("run");
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/").resourceChain(false);
	}// end of addResourceHandlers
	
}
