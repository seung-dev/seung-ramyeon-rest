package app.boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import app.boot.configuration.type.SEnvironment;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.types.SBuildProperties;
import seung.util.kimchi.types.SLinkedHashMap;

@PropertySources({
	@PropertySource(value = "classpath:s-security-${spring.profiles.active}.properties")
	, @PropertySource(value = "classpath:s-datasource-${spring.profiles.active}.properties")
	, @PropertySource(value = "classpath:s-application-${spring.profiles.active}.properties")
	, @PropertySource(value = "file:${app.path}/${spring.application.name}.properties", ignoreResourceNotFound=true)
})
@ComponentScan(value = {"app"})
@Configuration
@Slf4j
public class SConfiguration {

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
		return new SEnvironment();
	}// end of sEnv
	
	@Bean(name = "sCache")
	public SLinkedHashMap SCache() {
		return new SLinkedHashMap();
	}// end of sCache
	
}
