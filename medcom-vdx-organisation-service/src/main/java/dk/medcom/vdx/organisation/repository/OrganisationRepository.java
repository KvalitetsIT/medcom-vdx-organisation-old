package dk.medcom.vdx.organisation.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import dk.medcom.vdx.organisation.dao.Organisation;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {
	
	public Organisation findByOrganisationId(String organisationId);

	public List<Organisation> findByPoolSizeNotNull();
}
