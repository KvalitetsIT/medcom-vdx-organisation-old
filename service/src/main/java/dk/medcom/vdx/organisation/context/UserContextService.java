package dk.medcom.vdx.organisation.context;

import java.util.List;

public interface UserContextService {
	
	String getOrganisation();
	
	boolean hasAnyNumberOfRoles(List<UserRole> allowed);
}
