package dk.medcom.vdx.organisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import dk.medcom.vdx.organisation.configuration.ContextConfiguration;
import dk.medcom.vdx.organisation.configuration.DatabaseConfiguration;
import dk.medcom.vdx.organisation.configuration.ServiceConfiguration;

@EnableAutoConfiguration
@Configuration
@Import({ DatabaseConfiguration.class, ContextConfiguration.class, ServiceConfiguration.class })
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
