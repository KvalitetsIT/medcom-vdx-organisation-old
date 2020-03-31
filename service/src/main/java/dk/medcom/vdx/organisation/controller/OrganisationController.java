package dk.medcom.vdx.organisation.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.aspect.APISecurityAnnotation;
import dk.medcom.vdx.organisation.context.UserRole;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.service.CreateOrUpdateOrganisationService;
import dk.medcom.vdx.organisation.service.FindOrganisationService;

@RestController
public class OrganisationController {

	private static Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

	@Autowired
	FindOrganisationService findOrganisationService;
	
	@Autowired
	CreateOrUpdateOrganisationService createOrUpdateOrganisationService; 

	@APISecurityAnnotation({ UserRole.USER, UserRole.ADMIN, UserRole.PROVISIONER })
	@RequestMapping(value = "/services/organisation", method = RequestMethod.GET)
	public List<OrganisationDto> getOrganisations() throws PermissionDeniedException {
		LOGGER.debug("Enter getOrganisations");
		try {
			List<OrganisationDto> organisations = findOrganisationService.findOrganisations();
			return organisations;
		} finally {
			LOGGER.debug("Done getOrganisations");
		}
	}

	@APISecurityAnnotation({ UserRole.USER, UserRole.ADMIN, UserRole.PROVISIONER })
	@RequestMapping(value = "/services/organisation/{shortName}", method = RequestMethod.GET)
	public OrganisationDto getOrganisation(@PathVariable("shortName")String shortName) throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		LOGGER.debug("Enter getOrganisation(shortName: "+shortName+")");
		try {
			OrganisationDto organisation = findOrganisationService.findOrganisationFromShortName(shortName);
			return organisation;
		} finally {
			LOGGER.debug("Done getOrganisation(shortName: "+shortName+")");
		}
	}
	
	@APISecurityAnnotation({ UserRole.ADMIN, UserRole.PROVISIONER })
	@RequestMapping(value = "/services/organisation", method = RequestMethod.POST)
	public OrganisationDto createOrganisation(/*@Valid*/ @RequestBody OrganisationDto toCreate) throws PermissionDeniedException, BadRequestException {
		LOGGER.debug("Enter createOrganisation(toCreate: "+toCreate+")");
		try {
			OrganisationDto organisation = createOrUpdateOrganisationService.createOrganisation(toCreate);
			return organisation;
		} finally {
			LOGGER.debug("Done createOrganisation(toCreate: "+toCreate+")");
		}
	}
}
