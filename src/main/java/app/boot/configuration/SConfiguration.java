package app.boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import app.boot.configuration.types.SEnvironment;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SBuildProperties;
import seung.util.kimchi.types.SLinkedHashMap;

@PropertySources({
	@PropertySource(value = "classpath:profile-${spring.profiles.active}/s-application-${spring.profiles.active}.properties")
	, @PropertySource(value = "file:${app.path}/${spring.application.name}.properties", ignoreResourceNotFound=true)
	, @PropertySource(value = "classpath:profile-${spring.profiles.active}/s-security-${spring.profiles.active}.properties")
	, @PropertySource(value = "classpath:profile-${spring.profiles.active}/s-datasource-${spring.profiles.active}.properties")
})
@ComponentScan(value = {"app"})
@Configuration
@Slf4j
public class SConfiguration {

	@Autowired
	private Environment environment;
	
	@Autowired(required = false)
	private BuildProperties buildProperties;
	
	@Bean(name = "sBuildProperties")
	public SBuildProperties sBuildProperties() {
		log.debug("run");
		if(buildProperties == null) {
			return SBuildProperties.builder().build();
		}
		return SBuildProperties.builder()
				.build_group(buildProperties.getGroup())
				.build_artifact(buildProperties.getArtifact())
				.build_name(buildProperties.getName())
				.build_version(buildProperties.getVersion())
				.build_time(buildProperties.get("time"))
				.build();
	}// end of sBuildProperties
	
	@Bean(name = "sEnvironment")
	public SEnvironment sEnvironment() {
		return new SEnvironment(environment);
	}// end of sEnvironment
	
	@Bean(name = "sCache")
	public SLinkedHashMap SCache() {
		return new SLinkedHashMap();
	}// end of sCache
	
}
