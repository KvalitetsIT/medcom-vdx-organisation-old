package dk.medcom.vdx.organisation.dao;

import java.util.List;

import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.exceptions.DataIntegretyException;

public interface OrganisationDao {

	List<Organisation> findOrganisations();

	List<Organisation> findOrganisations(String topOrganisationCode);

	List<Organisation> findAncestorsOrderedByDistanceClosestFirst(Long organisationId);

	Organisation findByOrganisationCode(String organisationCode);

	List<Organisation> findByPoolSizeGreaterThanZero();
	
	Organisation updateOrganisationWithCode(String organisationCode, String organisationName, int poolSize, List<Organisation> newAncestorsOrderedByDistanceClosestFirst, List<Organisation> oldAncestorsOrderedByDistanceClosestFirst);

	Organisation createOrganisation(List<Organisation> ancestorsOrderedByDistanceClosestFirst, String organisationCode, String organisationName, int poolSize) throws DataIntegretyException;

	void deleteOrganisation(Organisation orgToDelete);
}
