package dk.medcom.vdx.organisation.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.DataIntegretyException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.service.CreateOrUpdateOrganisationService;

@Service
@Transactional
public class CreateOrUpdateOrganisationServiceImpl extends AbstractOrganisationServiceImpl implements CreateOrUpdateOrganisationService {

	private static Logger LOGGER = LoggerFactory.getLogger(CreateOrUpdateOrganisationServiceImpl.class);

	private UserContextService userContextService;

	private OrganisationDao organisationDao;
	
	public CreateOrUpdateOrganisationServiceImpl(UserContextService userContextService, OrganisationDao organisationDao) {
		this.userContextService = userContextService;
		this.organisationDao = organisationDao;
	}

	@Override
	public Organisation updateOrganisation(OrganisationDto toUpdate) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException, DataIntegretyException {
		
		List<Organisation> newAncestorsOrderedByDistanceClosestFirst = validateOrganisationInput(toUpdate);
		
		Organisation orgToUpdate = organisationDao.findByOrganisationCode(toUpdate.getCode());
		if (orgToUpdate == null) {
			String message = "The code: "+toUpdate.getCode()+" does not identify an organisation";
			LOGGER.info(message);
			throw new RessourceNotFoundException(message);
		}

		// Check if the user belongs to the right organisations for a legal update
		String userOrganisation = userContextService.getOrganisation();
		boolean updateIsOk = noOrganisation(userOrganisation);

		String newParentCode = (toUpdate.getParentCode() != null ? toUpdate.getParentCode().strip() : "");
		String oldParentCode = (orgToUpdate.getParentOrganisationCode() != null ? orgToUpdate.getParentOrganisationCode() : "");
		boolean movingTheOrganisationInTheTree = !newParentCode.contentEquals(oldParentCode);
		List<Organisation> oldAncestorsOrderedByDistanceClosestFirst = null;
		if (movingTheOrganisationInTheTree || !updateIsOk) {
			oldAncestorsOrderedByDistanceClosestFirst = organisationDao.findAncestorsOrderedByDistanceClosestFirst(orgToUpdate.getParentOrganisationId());
		}

		// Check that we are not moving a node to its own subnode
		if (movingTheOrganisationInTheTree && newAncestorsOrderedByDistanceClosestFirst != null) {

			for (Organisation newAncestor : newAncestorsOrderedByDistanceClosestFirst) {
				if (newAncestor.getId().equals(orgToUpdate.getId())) {
					String message = "The organisation identified by '"+toUpdate.getCode()+"' cannot be moved to its own suborganisation '"+toUpdate.getParentCode()+"'";
					LOGGER.info(message);
					throw new DataIntegretyException(message);
				}
			}
			
		}
		
		if (!updateIsOk) {
			updateIsOk = isOrganisationPartOfOrganisation(userOrganisation, orgToUpdate.getOrganisationId()) || isOrganisationPartOfAnyOrganisation(userOrganisation, oldAncestorsOrderedByDistanceClosestFirst);
			
			if (movingTheOrganisationInTheTree) {
				updateIsOk = updateIsOk && (isOrganisationPartOfOrganisation(userOrganisation, toUpdate.getCode()) || isOrganisationPartOfAnyOrganisation(userOrganisation, newAncestorsOrderedByDistanceClosestFirst));
			}
		}

		if (!updateIsOk) {
			String message = "The user does not have access to the organisation identified by '"+toUpdate.getCode()+"' "+(movingTheOrganisationInTheTree ? "or the organisation identified by '"+toUpdate.getParentCode()+"'" : "");
			LOGGER.info(message);
			throw new PermissionDeniedException(message);
		}

		Organisation updated = organisationDao.updateOrganisationWithCode(toUpdate.getCode(), 
				toUpdate.getName(), 
				toUpdate.getPoolSize(), 
				(movingTheOrganisationInTheTree ? newAncestorsOrderedByDistanceClosestFirst : null), 
				(movingTheOrganisationInTheTree ? oldAncestorsOrderedByDistanceClosestFirst : null));
		return updated;
	}
	
	@Override
	public Organisation createOrganisation(OrganisationDto toCreate) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException, DataIntegretyException {

		List<Organisation> ancestorsOrderedByDistanceClosestFirst = validateOrganisationInput(toCreate);
		
		String userOrganisation = userContextService.getOrganisation();
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, toCreate.getCode()) && !isOrganisationPartOfAnyOrganisation(userOrganisation, ancestorsOrderedByDistanceClosestFirst)) {
			String message = "The user does not have access to the organisation identified by '"+toCreate.getCode()+"'";
			LOGGER.info(message);
			throw new PermissionDeniedException(message);
		}
		
		Organisation created = organisationDao.createOrganisation(ancestorsOrderedByDistanceClosestFirst, toCreate.getCode(), toCreate.getName(), toCreate.getPoolSize());
		return created;
	}

	@Override
	public void deleteOrganisationWithCode(String codeToDelete) throws PermissionDeniedException, BadRequestException, RessourceNotFoundException {
		if (codeToDelete == null || codeToDelete.length() == 0) {
			String message = "Deleting an organisation must be identified by 'code'";
			LOGGER.info(message);
			throw new BadRequestException(message);
		}
		
		Organisation orgToDelete = organisationDao.findByOrganisationCode(codeToDelete);
		if (orgToDelete == null) {
			String message = "The code: "+codeToDelete+" does not identify an organisation";
			LOGGER.info(message);
			throw new RessourceNotFoundException(message);
		}
		
		String userOrganisation = userContextService.getOrganisation();
		List<Organisation> parents = organisationDao.findAncestorsOrderedByDistanceClosestFirst(orgToDelete.getId());
		if (!noOrganisation(userOrganisation) && !isOrganisationPartOfOrganisation(userOrganisation, codeToDelete) && !isOrganisationPartOfAnyOrganisation(userOrganisation, parents)) {
			String message = "The user does not have access to the organisation identified by '"+codeToDelete+"'";
			LOGGER.info(message);
			throw new PermissionDeniedException(message);
		}
		
		organisationDao.deleteOrganisation(orgToDelete);
		
	}

	private List<Organisation> validateOrganisationInput(OrganisationDto input) throws BadRequestException, RessourceNotFoundException {
		if (input.getCode() == null || input.getCode().length() == 0) {
			String message = "An organisation must have a 'code'";
			LOGGER.info(message);
			throw new BadRequestException(message);
		}

		if (input.getName() == null || input.getName().length() == 0) {
			String message = "An organisation must have a 'name'";
			LOGGER.info(message);
			throw new BadRequestException(message);
		}

		if (input.getPoolSize() < 0) {
			String message = "An organisation must have a 'poolSize' > 0";
			LOGGER.info(message);
			throw new BadRequestException(message);
		}
		
		if (input.getParentCode() != null && input.getParentCode().strip().length() > 0) {
			Organisation parent = organisationDao.findByOrganisationCode(input.getParentCode());
			if (parent == null) {
				String message = "The code '"+input.getParentCode()+"' does not identify an organisation";
				LOGGER.info(message);
				throw new RessourceNotFoundException(message);
			}

			return organisationDao.findAncestorsOrderedByDistanceClosestFirst(parent.getId());
		}
		return null;
	}

}
