package dk.medcom.vdx.organisation.context;

import java.util.List;

import dk.medcom.vdx.organisation.context.impl.SessionData;

public interface UserContextService {

	SessionData getSessionData();
	
	String getOrganisation();
	
	boolean hasAnyNumberOfRoles(List<UserRole> allowed);
}
