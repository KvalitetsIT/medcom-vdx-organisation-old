package dk.medcom.vdx.organisation.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.medcom.vdx.organisation.api.OrganisationTreeDto;
import dk.medcom.vdx.organisation.aspect.APISecurityAnnotation;
import dk.medcom.vdx.organisation.context.UserRole;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.service.FindOrganisationService;

@RestController
public class OrganisationTreeController {

	private static Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

	@Autowired
	FindOrganisationService findOrganisationService;

	@APISecurityAnnotation({ UserRole.USER, UserRole.ADMIN, UserRole.PROVISIONER })
	@RequestMapping(value = "/services/organisationtree", method = RequestMethod.GET)
	public List<OrganisationTreeDto> getOrganisationTrees() throws PermissionDeniedException {
		LOGGER.debug("Enter getOrganisationTrees");
		try {
			List<Organisation> organisations = findOrganisationService.findOrganisations();
			List<OrganisationTreeDto> trees = buildTreesFromList(organisations);
			return trees;
		} finally {
			LOGGER.debug("Done getOrganisationTrees");
		}
	}

	@APISecurityAnnotation({ UserRole.USER, UserRole.ADMIN, UserRole.PROVISIONER })
	@RequestMapping(value = "/services/organisationtree/{code}", method = RequestMethod.GET)
	public OrganisationTreeDto getOrganisationTree(@PathVariable("code")String code) throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		LOGGER.debug("Enter getOrganisationTree(code: "+code+")");
		try {
			List<Organisation> organisations = findOrganisationService.findOrganisations(code);
			List<OrganisationTreeDto> trees = buildTreesFromList(organisations);
			return (trees != null && trees.size() == 1 ? trees.get(0): null);
		} finally {
			LOGGER.debug("Done getOrganisationTree(code: "+code+")");
		}
	}
	
	public List<OrganisationTreeDto> buildTreesFromList(List<Organisation> organisations) {

		if (organisations == null || organisations.size() == 0) {
			return null;
		}
		
		List<OrganisationTreeDto> roots = new LinkedList<OrganisationTreeDto>();
		Map<String, OrganisationTreeDto> treeMap = new HashMap<String, OrganisationTreeDto>();
		Map<String, List<OrganisationTreeDto>> childMap = new HashMap<String, List<OrganisationTreeDto>>();
		List<String> orgCodesFound = new LinkedList<String>();
		// Build the tree nodes
		for (Organisation organisation : organisations) {
			
			OrganisationTreeDto tree = new OrganisationTreeDto();
			tree.setCode(organisation.getOrganisationId());
			tree.setName(organisation.getName());
			tree.setPoolSize(organisation.getPoolSize());
			treeMap.put(tree.getCode(), tree);
			orgCodesFound.add(tree.getCode());
			String parentCode = organisation.getParentOrganisationCode();
			if (parentCode == null) {
				roots.add(tree);
			} else {
				if (!childMap.containsKey(parentCode)) {
					childMap.put(parentCode, new LinkedList<OrganisationTreeDto>());
				}
				childMap.get(parentCode).add(tree);
			}
		}
		
		// Add the children
		for (String orgKey : treeMap.keySet()) {
			if (childMap.containsKey(orgKey)) {
				treeMap.get(orgKey).setChildren(childMap.get(orgKey));
			}
		}
		
		// Make sure that orphan nodes (that references parents not included in the result) are considered as roots
		Set<String> parentsReferencedButNotIncluded = new HashSet<String>(childMap.keySet());
		parentsReferencedButNotIncluded.removeAll(orgCodesFound);
		for (String parentReferencedButNotIncluded : parentsReferencedButNotIncluded) {
			for (OrganisationTreeDto orphan : childMap.get(parentReferencedButNotIncluded)) {
				roots.add(orphan);
			}
		}
		
		return roots;
	}
}
