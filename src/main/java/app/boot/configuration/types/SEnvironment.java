package app.boot.configuration.types;

import org.springframework.core.env.Environment;

import seung.util.kimchi.types.SType;

public class SEnvironment extends SType {

	// os
	private final String os_name;
	private final boolean is_windows;
	private final boolean is_linux;
	private final boolean is_mac;
	private final String os_ver;
	
	// mode
	private final String app_mode;
	private final boolean is_ops;
	private final boolean is_dev;
	private final boolean is_loc;
	
	// server
	private final String host_name;
	
	// app
	private final String app_name;
	private final String app_port;
	
	// nas
	private final String nas_path;
	
	// shutdown
	private boolean shutdown;
	private String shutdown_message = "";
	
	public SEnvironment(
			String os_name
			, boolean is_windows
			, boolean is_linux
			, boolean is_mac
			, String os_ver
			, String app_mode
			, boolean is_ops
			, boolean is_dev
			, boolean is_loc
			, String host_name
			, String app_name
			, String app_port
			, String nas_path
			, boolean shutdown
			, String shutdown_message
			) {
		this.os_name = os_name;
		this.is_windows = is_windows;
		this.is_linux = is_linux;
		this.is_mac = is_mac;
		this.os_ver = os_ver;
		this.app_mode = app_mode;
		this.is_ops = is_ops;
		this.is_dev = is_dev;
		this.is_loc = is_loc;
		this.host_name = host_name;
		this.app_name = app_name;
		this.app_port = app_port;
		this.nas_path = nas_path;
		this.shutdown = shutdown;
		this.shutdown_message = shutdown_message;
	}
	
	public SEnvironment(
			String os_name
			, String os_ver
			, String app_mode
			, String host_name
			, String app_name
			, String app_port
			, String nas_path_windows
			, String nas_path_linux
			, String nas_path_mac
			) {
		this(os_name
				, os_name.toLowerCase().contains("win")//is_windows
				, os_name.toLowerCase().contains("linux")//is_linux
				, os_name.toLowerCase().contains("mac")//is_mac
				, os_ver
				, app_mode
				, "ops".equals(app_mode)//is_ops
				, "dev".equals(app_mode)//is_dev
				, "loc".equals(app_mode)//is_loc
				, host_name
				, app_name
				, app_port
				, os_name.toLowerCase().contains("win") ? nas_path_windows
						: os_name.toLowerCase().contains("linux") ? nas_path_linux
						: os_name.toLowerCase().contains("mac") ? nas_path_mac
						: ""//nas_path
				, false//shutdown
				, ""//shutdown_message
				);
	}
	
	public SEnvironment(
			Environment environment
			) {
		this(environment.getProperty("os.name", "")//os_name
				, environment.getProperty("os.version", "")//os_ver
				, environment.getProperty("spring.profiles.active", "")//app_mode
				, environment.getProperty("server.hostname", "")//host_name
				, environment.getProperty("spring.application.name", "")//app_name
				, environment.getProperty("server.port")//app_port
				, environment.getProperty("app.nas.path.windows")//nas_path_windows
				, environment.getProperty("app.nas.path.linux")//nas_path_linux
				, environment.getProperty("app.nas.path.mac")//nas_path_mac
				);
	}
	
	public String os_name() {
		return this.os_name;
	}
	
	public String os_ver() {
		return this.os_ver;
	}
	
	public boolean is_windows() {
		return this.is_windows;
	}
	
	public boolean is_linux() {
		return this.is_linux;
	}
	
	public boolean is_mac() {
		return this.is_mac;
	}
	
	public String app_mode() {
		return this.app_mode;
	}
	
	public boolean is_ops() {
		return this.is_ops;
	}
	
	public boolean is_dev() {
		return this.is_dev;
	}
	
	public boolean is_loc() {
		return this.is_loc;
	}
	
	public String host_name() {
		return this.host_name;
	}
	
	public String app_name() {
		return this.app_name;
	}
	
	public String app_port() {
		return this.app_port;
	}
	
	public String nas_path() {
		return this.nas_path;
	}
	
	public void shutdown(boolean shutdown, String shutdown_message) {
		this.shutdown = shutdown;
		this.shutdown_message = shutdown_message;
	}
	
	public boolean shutdown() {
		return this.shutdown;
	}
	
	public String shutdown_message() {
		return this.shutdown_message;
	}
	
}
