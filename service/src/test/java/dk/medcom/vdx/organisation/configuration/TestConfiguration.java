package dk.medcom.vdx.organisation.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "dk.medcom.vdx.organisation")
@PropertySource("test.properties")
public class TestConfiguration {
}
