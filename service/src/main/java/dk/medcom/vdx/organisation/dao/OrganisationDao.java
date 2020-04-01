package dk.medcom.vdx.organisation.dao;

import java.util.List;

import dk.medcom.vdx.organisation.dao.entity.Organisation;

public interface OrganisationDao {

	List<Organisation> findOrganisations();

	List<Organisation> findOrganisations(String topOrganisationCode);

	List<Organisation> findAncestorsOrderedByDistanceClosestFirst(Long organisationId);

	Organisation findByOrganisationCode(String organisationCode);

	List<Organisation> findByPoolSizeGreaterThanZero();
	
	Organisation updateOrganisationWithCode(String organisationCode, String organisationName, int poolSize, List<Organisation> newAncestorsOrderedByDistanceClosestFirst);

	Organisation createOrganisation(List<Organisation> ancestorsOrderedByDistanceClosestFirst, String organisationCode, String organisationName, int poolSize);
}
