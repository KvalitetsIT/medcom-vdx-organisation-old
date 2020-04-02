package dk.medcom.vdx.organisation.service.impl;

import java.util.List;

import dk.medcom.vdx.organisation.dao.entity.Organisation;

public class AbstractOrganisationServiceImpl {

	public boolean noOrganisation(String org) {
		return (org == null || org.strip().length() == 0);
	}
	
	public boolean isOrganisationPartOfOrganisation(String organisationCodeToCheck, String targetOrganisationCode) {
		if (organisationCodeToCheck == null || !organisationCodeToCheck.equals(targetOrganisationCode)) {
			return false;
		}
		return true;
	}
	
	
	public boolean isOrganisationPartOfAnyOrganisation(String organisationCodeToCheck, List<Organisation> ancestorsOrderedByDistance) {
		
		if (ancestorsOrderedByDistance == null || ancestorsOrderedByDistance.size() == 0) {
			return false;
		}
		for (Organisation organisation : ancestorsOrderedByDistance) {
			if (isOrganisationPartOfOrganisation(organisationCodeToCheck, organisation.getOrganisationId())) {
				return true;
			}
		}
		return false;
	}

	public boolean isAccessOk(String userOrganisation, Organisation organisationToCheck, List<Organisation> ancestorsOfOrganisationToCheck) {
		return noOrganisation(userOrganisation) || isOrganisationPartOfOrganisation(userOrganisation, organisationToCheck.getOrganisationId()) || isOrganisationPartOfAnyOrganisation(userOrganisation, ancestorsOfOrganisationToCheck);
	}
}
