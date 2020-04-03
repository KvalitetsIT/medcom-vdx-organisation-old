package dk.medcom.vdx.organisation.service;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.DataIntegretyException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;

public interface CreateOrUpdateOrganisationService {

	Organisation updateOrganisation(OrganisationDto toUpdate) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException, DataIntegretyException;
	
	Organisation createOrganisation(OrganisationDto createUpdate) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException;
}
