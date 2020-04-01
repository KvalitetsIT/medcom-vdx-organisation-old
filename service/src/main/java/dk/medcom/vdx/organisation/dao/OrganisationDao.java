package dk.medcom.vdx.organisation.dao;

import dk.medcom.vdx.organisation.dao.entity.Organisation;

import java.util.List;

public interface OrganisationDao {

	List<Organisation> findOrganisations();

	List<Organisation> findOrganisations(String topOrganisationCode);

	Organisation findByOrganisationCode(String organisationCode);

	List<Organisation> findByPoolSizeNotNull();
	
	Organisation updateOrganisationWithCode(String organisationCode, String organisationName, int poolSize);

	Organisation createOrganisation(String organisationCode, String organisationName, int poolSize);
}
