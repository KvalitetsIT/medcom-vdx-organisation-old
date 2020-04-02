package dk.medcom.vdx.organisation.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.service.FindOrganisationService;

@Service
public class FindOrganisationServiceImpl extends AbstractOrganisationServiceImpl implements FindOrganisationService {

	private static Logger LOGGER = LoggerFactory.getLogger(FindOrganisationServiceImpl.class);

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
			String message = "'code' must be provided in search";
			LOGGER.info(message);
			throw new BadRequestException(message);
		}
		Organisation foundOrg = organisationDao.findByOrganisationCode(code);
		if (foundOrg == null) {
			String message = "The code: "+code+" does not identify an organisation";
			LOGGER.info(message);
			throw new RessourceNotFoundException(message);
		}

		// Check if access is ok
		if (checkAccess) {
			String userOrganisation = userContextService.getOrganisation();
			List<Organisation> ancestorsOrderedByDistanceClosestFirst = null;
			if (foundOrg.getParentOrganisationId() != null) {
				ancestorsOrderedByDistanceClosestFirst = organisationDao.findAncestorsOrderedByDistanceClosestFirst(foundOrg.getParentOrganisationId());
			}
			if (!isAccessOk(userOrganisation, foundOrg, ancestorsOrderedByDistanceClosestFirst)) {			
				String message = "The user does not have access to the organisation identified by '"+code+"'";
				LOGGER.info(message);
				throw new PermissionDeniedException(message);
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
