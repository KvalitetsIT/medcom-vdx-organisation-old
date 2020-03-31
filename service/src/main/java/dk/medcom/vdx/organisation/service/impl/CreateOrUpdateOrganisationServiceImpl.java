package dk.medcom.vdx.organisation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
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
	public OrganisationDto updateOrganisation(OrganisationDto toUpdate) throws PermissionDeniedException, BadRequestException {
		
		validateOrganisationInput(toUpdate);
		
		String userOrganisation = userContextService.getOrganisation();
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, toUpdate.getShortName())) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+toUpdate.getShortName()+"'");
		}

		Organisation updated = organisationDao.updateOrganisationWithShortName(toUpdate.getShortName(), toUpdate.getName(), toUpdate.getPoolSize());
		return mapFromEntity(updated);
	}
	
	@Override
	public OrganisationDto createOrganisation(OrganisationDto toCreate) throws PermissionDeniedException, BadRequestException {

		validateOrganisationInput(toCreate);
		
		String userOrganisation = userContextService.getOrganisation();
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, toCreate.getShortName())) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+toCreate.getShortName()+"'");
		}
		Organisation updated = organisationDao.createOrganisation(toCreate.getShortName(), toCreate.getName(), toCreate.getPoolSize());
		return mapFromEntity(updated);
	}

	public void validateOrganisationInput(OrganisationDto input) throws BadRequestException {
		
		if (input.getShortName() == null || input.getShortName().length() == 0) {
			throw new BadRequestException("An organisation must have a 'shortName'");
		}

		if (input.getName() == null || input.getName().length() == 0) {
			throw new BadRequestException("An organisation must have a 'name'");
		}

		if (input.getPoolSize() < 0) {
			throw new BadRequestException("An organisation must have a 'poolSize' > 0");
		}
	}
}
