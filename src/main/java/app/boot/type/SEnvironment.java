package app.boot.type;

import lombok.Getter;
import lombok.Setter;
import seung.util.kimchi.types.SType;

@Setter
@Getter
public class SEnvironment extends SType {

	// os
	private String os_name;
	private String os_ver;
	private boolean is_windows = false;
	private boolean is_linux = false;
	private boolean is_mac = false;
	
	// mode
	private String app_mode;
	private boolean is_ops = false;
	private boolean is_dev = false;
	private boolean is_loc = false;
	
	// server
	private String host_name;
	private String public_ipv4;
	
	// app
	private String app_name;
	private int app_port;
	
	// nas
	private String nas_path;
	
	// shutdown
	private boolean shutdown = false;
	private String shutdown_message = "";
	
	public void shutdown(boolean shutdown, String shutdown_message) {
		this.shutdown = shutdown;
		this.shutdown_message = shutdown_message;
	}
	
}
