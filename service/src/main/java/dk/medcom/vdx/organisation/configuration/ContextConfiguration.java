package dk.medcom.vdx.organisation.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.context.impl.UserContextServiceImpl;

@Configuration
public class ContextConfiguration {
	@Value("${usercontext.header.name}")
	String userContextHeaderName;

	@Value("${userattributes.role.key}")
	String userAttributesRoleKey;

	@Value("${userattributes.org.key}")
	String userAttributesOrgKey;

	@Value("${userrole.admin.values}")
	List<String> userRoleAdminValues;

	@Value("${userrole.user.values}")
	List<String> userRoleUserValues;

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
	public UserContextService userContextService() {
		UserContextServiceImpl ucs = new UserContextServiceImpl(userContextHeaderName, userAttributesOrgKey, userAttributesRoleKey, userRoleAdminValues, userRoleUserValues);
		return ucs;
	}
}
