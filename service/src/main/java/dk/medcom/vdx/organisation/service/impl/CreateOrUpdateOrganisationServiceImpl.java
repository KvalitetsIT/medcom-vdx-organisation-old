package dk.medcom.vdx.organisation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.service.CreateOrUpdateOrganisationService;

public class CreateOrUpdateOrganisationServiceImpl extends AbstractOrganisationServiceImpl implements CreateOrUpdateOrganisationService {

	private UserContextService userContextService;

	private OrganisationDao organisationDao;
	
	@Autowired
	public CreateOrUpdateOrganisationServiceImpl(UserContextService userContextService, OrganisationDao organisationDao) {
		this.userContextService = userContextService;
		this.organisationDao = organisationDao;
	}

	@Override
	public OrganisationDto updateOrganisation(OrganisationDto toUpdate) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException {
		
		List<Organisation> newAncestorsOrderedByDistanceClosestFirst = validateOrganisationInput(toUpdate);
		
		String userOrganisation = userContextService.getOrganisation();
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, toUpdate.getCode())) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+toUpdate.getCode()+"'");
		}
		if (newAncestorsOrderedByDistanceClosestFirst != null && !noOrganisation(userOrganisation) && !isOrganisationPartOfAnyOrganisation(userOrganisation, newAncestorsOrderedByDistanceClosestFirst)) {
			throw new PermissionDeniedException("The user does not have access to the parent organisation identified by '"+toUpdate.getParentCode()+"'");
		}

		Organisation updated = organisationDao.updateOrganisationWithCode(toUpdate.getCode(), toUpdate.getName(), toUpdate.getPoolSize(), newAncestorsOrderedByDistanceClosestFirst);
		return mapFromEntity(updated);
	}
	
	@Override
	public OrganisationDto createOrganisation(OrganisationDto toCreate) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException {

		List<Organisation> ancestorsOrderedByDistanceClosestFirst = validateOrganisationInput(toCreate);
		
		String userOrganisation = userContextService.getOrganisation();
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, toCreate.getCode()) && !isOrganisationPartOfAnyOrganisation(userOrganisation, ancestorsOrderedByDistanceClosestFirst)) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+toCreate.getCode()+"'");
		}
		
		Organisation created = organisationDao.createOrganisation(ancestorsOrderedByDistanceClosestFirst, toCreate.getCode(), toCreate.getName(), toCreate.getPoolSize());
		return mapFromEntity(created);
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
