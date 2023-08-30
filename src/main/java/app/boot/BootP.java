package app.boot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import app.boot.configuration.type.SEnvironment;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import seung.util.kimchi.SSecurity;
import seung.util.kimchi.types.SBuildProperties;
import seung.util.kimchi.types.SLinkedHashMap;

@Component
@Slf4j
public class BootP {

	@Autowired
	private Environment environment;
	
	@Resource(name = "sBuildProperties")
	private SBuildProperties sBuildProperties;
	
	@Value(value = "${spring.profiles.active:#{null}}")
	private String app_mode;
	
	@Value(value = "${spring.application.name:#{null}}")
	private String app_name;
	
	@Value(value = "${server.port:#{null}}")
	private int server_port;
	
	@Value(value = "${app.nas.path.windows:#{null}}")
	private String nas_path_windows;
	
	@Value(value = "${app.nas.path.linux:#{null}}")
	private String nas_path_linux;
	
	@Value(value = "${app.nas.path.mac:#{null}}")
	private String nas_path_mac;
	
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
			
			// os
			String os_name = environment.getProperty("os.name", "");
			sEnvironment.setOs_name(os_name);
			sEnvironment.setOs_ver(environment.getProperty("os.version", ""));
			String os_name_lower = os_name.toLowerCase();
			if(os_name_lower.contains("win")) {
				sEnvironment.set_windows(true);
			} else if(os_name_lower.contains("linux")) {
				sEnvironment.set_linux(true);
			} else if(os_name_lower.contains("mac")) {
				sEnvironment.set_mac(true);
			}
			
			// mode
			sEnvironment.setApp_mode(app_mode);
			switch (app_mode) {
			case "ops":
				sEnvironment.set_ops(true);
				break;
			case "dev":
				sEnvironment.set_dev(true);
				break;
			case "loc":
				sEnvironment.set_loc(true);
				break;
			default:
				break;
			}
			
			// server
			sEnvironment.setHost_name(environment.getProperty("server.hostname"));
			if(sEnvironment.is_loc()) {
				sEnvironment.setPublic_ipv4("");
			} else {
				sEnvironment.setPublic_ipv4("");
			}
			
			// app
			sEnvironment.setApp_name(app_name);
			sEnvironment.setApp_port(server_port);
			
			// nas
			if(sEnvironment.is_windows()) {
				sEnvironment.setNas_path(nas_path_windows);
			} else if(sEnvironment.is_linux()) {
				sEnvironment.setNas_path(nas_path_linux);
			} else if(sEnvironment.is_mac()) {
				sEnvironment.setNas_path(nas_path_mac);
			}
			
		} catch (Exception e) {
			log.error("exception=", e);
		} finally {
			log.info("sEnvironment={}", sEnvironment.stringify(true));
			log.warn("sBuildProperties={}", sBuildProperties.stringify(true));
		}// end of try
		
	}// end of postRun
	
	@PreDestroy
	public void preDestroy() {
		log.warn("shutdown.environment={}", sEnvironment.stringify(false));
		log.warn("shutdown.message={}", sEnvironment.getShutdown_message());
	}// end of preDestroy
	
}
