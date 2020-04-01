package dk.medcom.vdx.organisation.service.impl;

import java.util.List;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.dao.entity.Organisation;

public class AbstractOrganisationServiceImpl {

	public boolean noOrganisation(String org) {
		return (org == null || org.length() == 0);
	}
	
	public boolean isOrganisationPartOfOrganisation(String organisationShortNameToCheck, String targetOrganisationShortName) {
		if (organisationShortNameToCheck == null || !organisationShortNameToCheck.equals(targetOrganisationShortName)) {
			return false;
		}
		return true;
	}
	
	public OrganisationDto mapFromEntity(Organisation organisation) {
		if (organisation != null) {
			OrganisationDto organisationDto = new OrganisationDto();
			organisationDto.setCode(organisation.getOrganisationId());
			organisationDto.setName(organisation.getName());
			organisationDto.setPoolSize(organisation.getPoolSize());
			return organisationDto;
		}
		return null;
	}
	
	public List<OrganisationDto> mapFromEntities(List<Organisation> organisations, List<OrganisationDto> result) {
		
		if (organisations == null || organisations.isEmpty()) {
			return result;
		}
		
		for (Organisation organisation : organisations) {
			result.add(mapFromEntity(organisation));
		}
		return result;
	}
}
