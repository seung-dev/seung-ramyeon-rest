package app.boot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import app.boot.configuration.types.SEnvironment;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.SSecurity;
import seung.util.kimchi.types.SBuildProperties;
import seung.util.kimchi.types.SLinkedHashMap;

@Component
@Slf4j
public class BootP {

	@Resource(name = "sBuildProperties")
	private SBuildProperties sBuildProperties;
	
	@Resource(name="sEnvironment")
	private SEnvironment sEnvironment;
	
	@Resource(name="sCache")
	private SLinkedHashMap sCache;
	
	@PostConstruct
	public void postConstruct() {
		log.debug("run");
		
		try {
			
			Unirest.config().verifySsl(false);
			SSecurity.add_bouncy_castle_provider();
			
			if(sEnvironment.is_loc()) {
				log.info("sEnvironment={}", sEnvironment.stringify(true));
			}
			
		} catch (Exception e) {
			log.error("exception=", e);
		} finally {
			log.warn("sBuildProperties={}", sBuildProperties.stringify(true));
		}// end of try
		
	}// end of postRun
	
	@PreDestroy
	public void preDestroy() {
		log.warn("shutdown.environment={}", sEnvironment.stringify(false));
		log.warn("shutdown.message={}", sEnvironment.shutdown_message());
	}// end of preDestroy
	
}
