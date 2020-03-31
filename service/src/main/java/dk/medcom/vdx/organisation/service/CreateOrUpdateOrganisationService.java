package dk.medcom.vdx.organisation.service;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;

public interface CreateOrUpdateOrganisationService {

	OrganisationDto updateOrganisation(OrganisationDto toUpdate) throws PermissionDeniedException, BadRequestException;
	
	OrganisationDto createOrganisation(OrganisationDto createUpdate) throws PermissionDeniedException, BadRequestException;
}
