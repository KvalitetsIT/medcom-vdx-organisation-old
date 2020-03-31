package dk.medcom.vdx.organisation.service;

import java.util.List;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;

public interface FindOrganisationService {

	public OrganisationDto findOrganisationFromShortName(String shortName) throws PermissionDeniedException, RessourceNotFoundException, BadRequestException;

	public List<OrganisationDto> findOrganisations() throws PermissionDeniedException;
}
