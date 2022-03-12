package pl.scf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ScfApplication {
	public static void main(String[] args) {
		SpringApplication.run(ScfApplication.class, args);
	}
}
