package dk.medcom.vdx.organisation.aspect;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.context.UserRole;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;

@Aspect
@Component
public class APISecurityAspect {
	
	static private List<UserRole> provisioners = new LinkedList<UserRole>();
	static {
		provisioners.add(UserRole.PROVISIONER);
	}
	
	@Autowired
	UserContextService userService;	
	
	
	@Before("@annotation(aPISecurityAnnotation)")
	public void APISecurityAnnotation(JoinPoint joinPoint, APISecurityAnnotation aPISecurityAnnotation) throws Throwable {

		UserRole[] allowedUserRoles = aPISecurityAnnotation.value();
		List<UserRole> allowed = Arrays.asList(allowedUserRoles);

		if (!userService.hasAnyNumberOfRoles(allowed)) {
			throw new PermissionDeniedException("The calling user does not have any of the required roles: "+allowedUserRoles);
		}

		String userOrganisation = userService.getOrganisation();
		if ((userOrganisation == null || userOrganisation.strip().length() == 0) && !userService.hasAnyNumberOfRoles(provisioners)) {
			throw new PermissionDeniedException("Only provisioners allowed to call without specifying organisation");
		}
    }
}