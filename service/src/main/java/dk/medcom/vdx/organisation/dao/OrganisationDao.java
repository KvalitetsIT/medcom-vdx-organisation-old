package dk.medcom.vdx.organisation.dao;

import dk.medcom.vdx.organisation.dao.entity.Organisation;

import java.util.List;

public interface OrganisationDao {
	Organisation findByOrganisationId(String organisationId);
	List<Organisation> findByPoolSizeNotNull();
}
