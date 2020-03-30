package dk.medcom.vdx.organisation.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;

public class AccessingUserInterceptor extends HandlerInterceptorAdapter {

	private static Logger LOGGER = LoggerFactory.getLogger(AccessingUserInterceptor.class);

	@Autowired
	UserContextService userContextService;

	@Autowired
	OrganisationDao organisationRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		// TODO parse usercontext og populer UserContextService ... hvis det ikke er sat, så returner false og put noget på response
		
		return true;
	}
}
