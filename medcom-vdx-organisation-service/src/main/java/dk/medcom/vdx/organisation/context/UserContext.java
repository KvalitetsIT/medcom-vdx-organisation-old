package dk.medcom.vdx.organisation.context;

import java.util.List;

public interface UserContext {

	String getUserOrganisation();
	
	String getUserEmail();
	
	List<UserRole> getUserRoles();	
}
