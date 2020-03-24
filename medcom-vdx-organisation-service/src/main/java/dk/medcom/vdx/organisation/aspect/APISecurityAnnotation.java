package dk.medcom.vdx.organisation.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dk.medcom.vdx.organisation.context.UserRole;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface APISecurityAnnotation {
	public UserRole[] value() default UserRole.DEFAULT;
}