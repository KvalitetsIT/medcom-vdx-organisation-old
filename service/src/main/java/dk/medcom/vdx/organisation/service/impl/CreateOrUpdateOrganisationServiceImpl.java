package dk.medcom.vdx.organisation.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.service.CreateOrUpdateOrganisationService;

@Component
public class CreateOrUpdateOrganisationServiceImpl extends AbstractOrganisationServiceImpl implements CreateOrUpdateOrganisationService {

	private UserContextService userContextService;

	private OrganisationDao organisationDao;
	
	public CreateOrUpdateOrganisationServiceImpl(UserContextService userContextService, OrganisationDao organisationDao) {
		this.userContextService = userContextService;
		this.organisationDao = organisationDao;
	}

	@Override
	public Organisation updateOrganisation(OrganisationDto toUpdate) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException {
		
		List<Organisation> newAncestorsOrderedByDistanceClosestFirst = validateOrganisationInput(toUpdate);
		
		Organisation orgToUpdate = organisationDao.findByOrganisationCode(toUpdate.getCode());
		if (orgToUpdate == null) {
			throw new RessourceNotFoundException("organisation", "code");
		}

		// Check if the user belongs to the right organisations
		String userOrganisation = userContextService.getOrganisation();
		boolean updateIsOk = noOrganisation(userOrganisation);

		boolean movingTheOrganisationInTheTree = (toUpdate.getParentCode() == null || toUpdate.getParentCode().strip().length() == 0 ? (orgToUpdate.getParentOrganisationCode() != null) : (toUpdate.getParentCode().compareTo(orgToUpdate.getParentOrganisationCode()) != 0));
		List<Organisation> oldAncestorsOrderedByDistanceClosestFirst = null;
		if (movingTheOrganisationInTheTree || !updateIsOk) {
			oldAncestorsOrderedByDistanceClosestFirst = organisationDao.findAncestorsOrderedByDistanceClosestFirst(orgToUpdate.getParentOrganisationId());
		}

		if (!updateIsOk) {
			updateIsOk = isOrganisationPartOfOrganisation(userOrganisation, orgToUpdate.getOrganisationId()) || isOrganisationPartOfAnyOrganisation(userOrganisation, oldAncestorsOrderedByDistanceClosestFirst);
			
			if (movingTheOrganisationInTheTree) {
				updateIsOk = updateIsOk && (isOrganisationPartOfOrganisation(userOrganisation, toUpdate.getCode()) || isOrganisationPartOfAnyOrganisation(userOrganisation, newAncestorsOrderedByDistanceClosestFirst));
			}
		}

		if (!updateIsOk) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+toUpdate.getCode()+"' "+(movingTheOrganisationInTheTree ? "or the organisation identified by '"+toUpdate.getParentCode()+"'" : ""));
		}

		Organisation updated = organisationDao.updateOrganisationWithCode(toUpdate.getCode(), 
				toUpdate.getName(), 
				toUpdate.getPoolSize(), 
				(movingTheOrganisationInTheTree ? newAncestorsOrderedByDistanceClosestFirst : null), 
				(movingTheOrganisationInTheTree ? oldAncestorsOrderedByDistanceClosestFirst : null));
		return updated;
	}
	
	@Override
	public Organisation createOrganisation(OrganisationDto toCreate) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException {

		List<Organisation> ancestorsOrderedByDistanceClosestFirst = validateOrganisationInput(toCreate);
		
		String userOrganisation = userContextService.getOrganisation();
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, toCreate.getCode()) && !isOrganisationPartOfAnyOrganisation(userOrganisation, ancestorsOrderedByDistanceClosestFirst)) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+toCreate.getCode()+"'");
		}
		
		Organisation created = organisationDao.createOrganisation(ancestorsOrderedByDistanceClosestFirst, toCreate.getCode(), toCreate.getName(), toCreate.getPoolSize());
		return created;
	}

	public List<Organisation> validateOrganisationInput(OrganisationDto input) throws BadRequestException, RessourceNotFoundException {
		
		if (input.getCode() == null || input.getCode().length() == 0) {
			throw new BadRequestException("An organisation must have a 'code'");
		}

		if (input.getName() == null || input.getName().length() == 0) {
			throw new BadRequestException("An organisation must have a 'name'");
		}

		if (input.getPoolSize() < 0) {
			throw new BadRequestException("An organisation must have a 'poolSize' > 0");
		}
		
		if (input.getParentCode() != null && input.getParentCode().strip().length() > 0) {
			Organisation parent = organisationDao.findByOrganisationCode(input.getParentCode());
			if (parent == null) {
				throw new RessourceNotFoundException("organisation", "parentCode");
			}
			
			List<Organisation> ancestorsOrderedByDistanceClosestFirst = organisationDao.findAncestorsOrderedByDistanceClosestFirst(parent.getId());
			return ancestorsOrderedByDistanceClosestFirst;
		}
		return null;
	}
}
