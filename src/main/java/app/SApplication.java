package app;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SApplication {

	public static void main(String[] args) {
		try {
			System.setProperty("server.hostname", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}// end of try
		SpringApplication.run(SApplication.class, args);
	}// end of main
	
}
