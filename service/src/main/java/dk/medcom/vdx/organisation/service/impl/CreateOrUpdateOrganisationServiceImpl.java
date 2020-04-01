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
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, toUpdate.getCode())) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+toUpdate.getCode()+"'");
		}

		Organisation updated = organisationDao.updateOrganisationWithCode(toUpdate.getCode(), toUpdate.getName(), toUpdate.getPoolSize());
		return mapFromEntity(updated);
	}
	
	@Override
	public OrganisationDto createOrganisation(OrganisationDto toCreate) throws PermissionDeniedException, BadRequestException {

		validateOrganisationInput(toCreate);
		
		String userOrganisation = userContextService.getOrganisation();
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, toCreate.getCode())) {
			throw new PermissionDeniedException("The user does not have access to the organisation identified by '"+toCreate.getCode()+"'");
		}
		Organisation updated = organisationDao.createOrganisation(toCreate.getCode(), toCreate.getName(), toCreate.getPoolSize());
		return mapFromEntity(updated);
	}

	public void validateOrganisationInput(OrganisationDto input) throws BadRequestException {
		
		if (input.getCode() == null || input.getCode().length() == 0) {
			throw new BadRequestException("An organisation must have a 'code'");
		}

		if (input.getName() == null || input.getName().length() == 0) {
			throw new BadRequestException("An organisation must have a 'name'");
		}

		if (input.getPoolSize() < 0) {
			throw new BadRequestException("An organisation must have a 'poolSize' > 0");
		}
	}
}
