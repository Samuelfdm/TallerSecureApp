package edu.eci.arep.TallerSecureApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Collections;

@SpringBootApplication
public class TallerSecureApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TallerSecureApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", Integer.toString(getPort())));
		app.run(args);
	}

	static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}
		return 8443; // returns default port if heroku-port isn't set (i.e. on localhost)
	}
}