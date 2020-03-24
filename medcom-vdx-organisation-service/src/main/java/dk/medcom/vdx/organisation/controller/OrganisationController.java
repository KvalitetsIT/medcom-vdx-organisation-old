package dk.medcom.vdx.organisation.controller;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.medcom.vdx.organisation.aspect.APISecurityAnnotation;
import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.context.UserRole;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.model.OrganisationDto;

@RestController
public class OrganisationController {

	private static Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

	@Autowired
	UserContextService userContextService;
	
	@APISecurityAnnotation({ UserRole.USER, UserRole.ADMIN })
	@RequestMapping(value = "/services/organisation", method = RequestMethod.GET)
	public List<OrganisationDto> getOrganisations() throws PermissionDeniedException, RessourceNotFoundException {
		LOGGER.info("*************** 1");
		
		String userOrganisation = userContextService.getOrganisation();
		LOGGER.info("*************** 2"+userOrganisation);
		if (userOrganisation == null) {
			throw new PermissionDeniedException();
		}
		
		List<OrganisationDto> organisationDtos = new LinkedList<>();
		
		return organisationDtos;
	}

	@RequestMapping(value = "/temp", method = RequestMethod.GET)
	public String erstatMedFeksInfo() {
		
		return "TODO";
	}

}
