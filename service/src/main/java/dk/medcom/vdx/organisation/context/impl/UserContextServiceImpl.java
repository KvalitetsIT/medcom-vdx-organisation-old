package dk.medcom.vdx.organisation.context.impl;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.context.UserRole;

public class UserContextServiceImpl implements UserContextService {

	private static Logger LOGGER = LoggerFactory.getLogger(UserContextServiceImpl.class);
	
	private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;

	private String userContextHeaderName;
	
	private String userAttributesRoleKey;

	private String userAttributesOrgKey;

	private List<String> userRoleMonitorValues;

	private List<String> userRoleProvisionerValues;

	private List<String> userRoleAdminValues;

	private List<String> userRoleUserValues;

	private SessionData cached;
	
	public UserContextServiceImpl(String userContextHeaderName, String userAttributesOrgKey, String userAttributesRoleKey, List<String> userRoleAdminValues, List<String> userRoleUserValues, List<String> userRoleMonitorValues, List<String> userRoleProvisionerValues) {
		this.userContextHeaderName = userContextHeaderName;
		this.userAttributesRoleKey = userAttributesRoleKey;
		this.userRoleAdminValues = userRoleAdminValues;
		this.userRoleUserValues = userRoleUserValues;
		this.userAttributesOrgKey = userAttributesOrgKey;
		this.userRoleMonitorValues = userRoleMonitorValues;
		this.userRoleProvisionerValues = userRoleProvisionerValues;
	}
	
	@Override
	public boolean hasAnyNumberOfRoles(List<UserRole> allowed) {
		Set<UserRole> userRoles = getUserRoles();
		if (userRoles != null) {
			for (UserRole role : allowed) {
				if (userRoles.contains(role)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private Set<UserRole> getUserRoles() {
		SessionData sessionData = getSessionData();
		if (sessionData == null) {
			return null;
		}
		if (sessionData.getUserAttributes().containsKey(userAttributesRoleKey)) {
			Set<UserRole> result = new HashSet<UserRole>();
			List<String> userRolesFromContext = sessionData.getUserAttributes(userAttributesRoleKey);
			for (String userRoleFromContext : userRolesFromContext) {
				if (userRoleAdminValues.contains(userRoleFromContext)) {
					result.add(UserRole.ADMIN);
				}
				if (userRoleUserValues.contains(userRoleFromContext)) {
					result.add(UserRole.USER);
				}
				if(userRoleMonitorValues.contains(userRoleFromContext)) {
					result.add(UserRole.MONITOR);
				}
				if(userRoleProvisionerValues.contains(userRoleFromContext)) {
					result.add(UserRole.PROVISIONER);
				}
			}
			if (result.isEmpty()) {
				result.add(UserRole.DEFAULT);
			}
			return result;
		}
		return null;
		
	}

	public SessionData getSessionData() {
		if (cached == null) {
			HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String userContextHeaderValue = servletRequest.getHeader(userContextHeaderName);
			if (userContextHeaderValue != null && userContextHeaderValue.length() > 0) {
				String decoded = "";
				try {
					decoded = new String(Base64.getDecoder().decode(userContextHeaderValue));
				} catch (IllegalArgumentException e) {
					LOGGER.error("Failed to decode headervalue: "+userContextHeaderValue);
					return null;
				}
			    try {
			    	SessionData sessionData = mapper.readValue(decoded, SessionData.class);
					if (!sessionData.containsUserAttributes()) {
						return null;
					}
					cached = sessionData;
				} catch (IOException e) {
					LOGGER.error("Failed to parse headervalue: "+decoded);
				}
			}
		}

		return cached;
	}

	@Override
	public String getOrganisation() {
		SessionData sessionData = getSessionData();
		if (sessionData == null) {
			return null;
		}
		if (sessionData.getUserAttributes().containsKey(userAttributesOrgKey)) {
			String userOrg = sessionData.getUserAttribute(userAttributesOrgKey);
			return userOrg;
		}
		return null;
	}
}