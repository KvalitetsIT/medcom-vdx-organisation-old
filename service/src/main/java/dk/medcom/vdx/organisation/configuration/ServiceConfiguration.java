package dk.medcom.vdx.organisation.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.service.CreateOrUpdateOrganisationService;
import dk.medcom.vdx.organisation.service.FindOrganisationService;
import dk.medcom.vdx.organisation.service.impl.CreateOrUpdateOrganisationServiceImpl;
import dk.medcom.vdx.organisation.service.impl.FindOrganisationServiceImpl;

@Configuration
@EnableAspectJAutoProxy
public class ServiceConfiguration implements WebMvcConfigurer {

	private static Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class);

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.debug("Adding interceptors");
	} 

/*	@Bean
	public LoggingInterceptor loggingInterceptor() {
		LOGGER.debug("Creating loggingInterceptor");
		return new LoggingInterceptor();
	}
*/	
	
	@Bean
	public FindOrganisationService findOrganisationService(UserContextService userContextService, OrganisationDao organisationDao) {
		return new FindOrganisationServiceImpl(userContextService, organisationDao);
	}
	
	@Bean
	public CreateOrUpdateOrganisationService createOrUpdateOrganisationService(UserContextService userContextService, OrganisationDao organisationDao) {
		return new CreateOrUpdateOrganisationServiceImpl(userContextService, organisationDao);
	}

}