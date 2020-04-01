package dk.medcom.vdx.organisation.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.service.FindOrganisationService;

public class FindOrganisationServiceImpl extends AbstractOrganisationServiceImpl implements FindOrganisationService {

	private UserContextService userContextService;

	private OrganisationDao organisationDao;
	
	@Autowired
	public FindOrganisationServiceImpl(UserContextService userContextService, OrganisationDao organisationDao) {
		this.userContextService = userContextService;
		this.organisationDao = organisationDao;
	}
	
	@Override
	public OrganisationDto findOrganisationFromShortName(String shortName) throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		if (shortName == null || shortName.length() == 0) {
			throw new BadRequestException("'shortName' must be provided in search");
		}
		
		String userOrganisation = userContextService.getOrganisation();
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, shortName)) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+shortName+"'");
		}
		
		Organisation foundOrg = organisationDao.findByOrganisationShortName(shortName);
		if (foundOrg == null) {
			throw new RessourceNotFoundException("organisation", "shortname");
		}
		return mapFromEntity(foundOrg);
	}

	@Override
	public List<OrganisationDto> findOrganisations() throws PermissionDeniedException {
		
		String userOrganisation = userContextService.getOrganisation();
		List<Organisation> foundOrgs = null;
		
		if (noOrganisation(userOrganisation)) {
			foundOrgs = organisationDao.findOrganisations();
		} else {
			foundOrgs = organisationDao.findOrganisations(userOrganisation);
		}

		return mapFromEntities(foundOrgs, new LinkedList<OrganisationDto>());
	}
}