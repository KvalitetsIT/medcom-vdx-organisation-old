package dk.medcom.vdx.organisation.controller;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.model.OrganisationDto;

@RestController
public class OrganisationController {

	private static Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);
	
	@RequestMapping(value = "/services/organisation", method = RequestMethod.GET)
	public List<OrganisationDto> getOrganisations() throws PermissionDeniedException, RessourceNotFoundException {
		
		LOGGER.info("*************");
		
		List<OrganisationDto> organisationDtos = new LinkedList<>();
		CollectionModel<OrganisationDto> resources = new CollectionModel<OrganisationDto>(organisationDtos);
		
		return organisationDtos;
	}
}
