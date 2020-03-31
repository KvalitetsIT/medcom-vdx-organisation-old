package dk.medcom.vdx.organisation.dao;

import dk.medcom.vdx.organisation.dao.entity.Organisation;

import java.util.List;

public interface OrganisationDao {

	List<Organisation> findOrganisations();

	List<Organisation> findOrganisations(String topOrganisationShortName);

	Organisation findByOrganisationShortName(String organisationShortName);

	List<Organisation> findByPoolSizeNotNull();
	
	Organisation updateOrganisationWithShortName(String organisationShortName, String organisationName, int poolSize);

	Organisation createOrganisation(String organisationShortName, String organisationName, int poolSize);
}
