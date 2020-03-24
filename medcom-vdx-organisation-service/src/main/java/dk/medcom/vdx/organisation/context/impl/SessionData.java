package dk.medcom.vdx.organisation.context.impl;

import java.util.List;
import java.util.Map;

public class SessionData {

	public Map<String,List<String>> UserAttributes;

	public Map<String, List<String>> getUserAttributes() {
		return UserAttributes;
	}

	public void setUserAttributes(Map<String, List<String>> userAttributes) {
		UserAttributes = userAttributes;
	}

	public List<String> getUserAttributes(String userAttribute) {
		if (UserAttributes != null && UserAttributes.containsKey(userAttribute)) {
			List<String> ual = UserAttributes.get(userAttribute);
			if (ual != null && ual.size() > 0) {
				return ual;
			}
		}
		return null;
	}
	
	public boolean containsUserAttributes() {
		return UserAttributes != null;
	}

	public String getUserAttribute(String userAttribute) {
		List<String> userAttributes = getUserAttributes(userAttribute);
		if (userAttributes != null && userAttributes.size() > 0) {
			return userAttributes.get(0);
		}
		return null;
	}
}
