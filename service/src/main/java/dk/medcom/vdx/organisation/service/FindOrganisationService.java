package dk.medcom.vdx.organisation.service;

import java.util.List;

import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;

public interface FindOrganisationService {

	public Organisation findOrganisationFromCode(String code) throws PermissionDeniedException, RessourceNotFoundException, BadRequestException;

	public List<Organisation> findOrganisations() throws PermissionDeniedException;
	
	public List<Organisation> findOrganisations(String code) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException;

}
