package dk.medcom.vdx.organisation.configuration;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.service.actuator.ActuatorHttpFilter;
import dk.medcom.vdx.organisation.service.actuator.VersionInfoContributor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.http.HttpFilter;
import java.util.List;

@Configuration
@PropertySource("actuator.properties")
@PropertySource(value = "git.properties", ignoreResourceNotFound = true)
public class ActuatorConfiguration {
    @Bean
    public FilterRegistrationBean<HttpFilter> actuatorSecurityFilter(UserContextService userContextService) {
        var filterRegistrationBean = new FilterRegistrationBean<HttpFilter>();
        filterRegistrationBean.setFilter(new ActuatorHttpFilter(userContextService));
        filterRegistrationBean.addUrlPatterns("/actuator/*");

        return filterRegistrationBean;
    }

    @Bean
    public VersionInfoContributor versionInfo(@Value("${git.commit.id.describe:#{null}}")String commit, @Value("${git.tags:#{null}}") List<String> tags) {
        return new VersionInfoContributor(commit, tags);
    }
}
