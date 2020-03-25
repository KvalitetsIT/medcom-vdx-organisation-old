package dk.medcom.vdx.organisation.configuration;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.context.UserRole;
import dk.medcom.vdx.organisation.interceptor.AccessingUserInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableAspectJAutoProxy
public class ServiceConfiguration implements WebMvcConfigurer {

	private static Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.debug("Adding interceptors");
//		registry.addInterceptor(loggingInterceptor());
		registry.addInterceptor(userSecurityInterceptor());
	} 

/*	@Bean
	public LoggingInterceptor loggingInterceptor() {
		LOGGER.debug("Creating loggingInterceptor");
		return new LoggingInterceptor();
	}
*/	
	@Bean	
	public AccessingUserInterceptor userSecurityInterceptor() {
		LOGGER.debug("Creating userSecurityInterceptor");
		return new AccessingUserInterceptor();
	}

	@Bean
	public FilterRegistrationBean actuatorSecurityFilter(UserContextService userContextService) {
		var filterRegistrationBean = new FilterRegistrationBean<HttpFilter>();
		filterRegistrationBean.setFilter(new HttpFilter() {
			@Override
			protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
				if(!userContextService.hasAnyNumberOfRoles(Arrays.asList(UserRole.USER))) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				}

				if(request.getRequestURI().contains("forbudt")) {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				}

				chain.doFilter(request, response);
			}
		});

		filterRegistrationBean.addUrlPatterns("/actuator/*");

		return filterRegistrationBean;
	}
}