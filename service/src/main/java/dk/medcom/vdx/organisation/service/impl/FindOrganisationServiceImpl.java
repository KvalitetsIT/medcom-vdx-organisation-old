package dk.medcom.vdx.organisation.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.service.FindOrganisationService;

@Component
public class FindOrganisationServiceImpl extends AbstractOrganisationServiceImpl implements FindOrganisationService {

	private UserContextService userContextService;

	private OrganisationDao organisationDao;

	public FindOrganisationServiceImpl(UserContextService userContextService, OrganisationDao organisationDao) {
		this.userContextService = userContextService;
		this.organisationDao = organisationDao;
	}

	@Override
	public Organisation findOrganisationFromCode(String code) throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {

		Organisation foundOrg = findOrganisationFromCode(code, true);
		return foundOrg;
	}

	public Organisation findOrganisationFromCode(String code, boolean checkAccess) throws BadRequestException, RessourceNotFoundException, PermissionDeniedException {

		if (code == null || code.length() == 0) {
			throw new BadRequestException("'code' must be provided in search");
		}
		Organisation foundOrg = organisationDao.findByOrganisationCode(code);
		if (foundOrg == null) {
			throw new RessourceNotFoundException("organisation", "code");
		}

		// Check if access is ok
		if (checkAccess) {
			String userOrganisation = userContextService.getOrganisation();
			List<Organisation> ancestorsOrderedByDistanceClosestFirst = null;
			if (foundOrg.getParentOrganisationId() != null) {
				ancestorsOrderedByDistanceClosestFirst = organisationDao.findAncestorsOrderedByDistanceClosestFirst(foundOrg.getParentOrganisationId());
			}
			if (!isAccessOk(userOrganisation, foundOrg, ancestorsOrderedByDistanceClosestFirst)) {			
				throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+code+"'");
			}
		}
		return foundOrg;
	}

	@Override
	public List<Organisation> findOrganisations() throws PermissionDeniedException {

		String userOrganisation = userContextService.getOrganisation();
		List<Organisation> foundOrgs = null;

		if (noOrganisation(userOrganisation)) {
			foundOrgs = organisationDao.findOrganisations();
		} else {
			foundOrgs = organisationDao.findOrganisations(userOrganisation);
		}

		return foundOrgs;
	}

	@Override
	public List<Organisation> findOrganisations(String code) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException {

		Organisation topOrg = findOrganisationFromCode(code, true);
		List<Organisation> foundOrgs = organisationDao.findOrganisations(topOrg.getOrganisationId());
		return foundOrgs;
	}
}
