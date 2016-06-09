package pl.edu.agh.metal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * Punkt centralny aplikacji i główna konfiguracja Springa. Aplikacja pakowana jest do pliku .jar,
 * który zawiera wbudowanego Tomcata.
 */
@SpringBootApplication
@ComponentScan()
public class ReplicationRestApplication {

	@Autowired
	private static Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(ReplicationRestApplication.class, args);
	}
}
