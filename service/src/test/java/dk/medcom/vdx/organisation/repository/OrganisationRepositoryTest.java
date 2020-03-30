package dk.medcom.vdx.organisation.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import dk.medcom.vdx.organisation.dao.JdbcOrganisationDao;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import org.junit.Assert;
import org.junit.Test;

import dk.medcom.vdx.organisation.dao.entity.Organisation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class OrganisationRepositoryTest extends RepositoryTest {

	@Autowired
    private OrganisationDao subject;

	@Test
	public void testFindOrganisationByExistingOrganisationId() {
		// Given
		String existingOrg = "Company 1";
		
		// When
		Organisation organisation = subject.findByOrganisationId(existingOrg);
		
		// Then
		Assert.assertNotNull(organisation);
	}
	
	@Test
	public void testFindOrganisationByNonExistingOrganisationId() {
		// Given
		String existingOrg = "nonexisting-org";
		
		// When
		Organisation organisation = subject.findByOrganisationId(existingOrg);
		
		// Then
		Assert.assertNull(organisation);
	}

	@Test
	public void testFindOrganizationWithPool() {
		// Given
		String existingOrg = "pool-test-org";

		// When
		Organisation organisation = subject.findByOrganisationId(existingOrg);

		// Then
		Assert.assertNotNull(organisation);
		assertEquals(10, organisation.getPoolSize().longValue());
		assertEquals("pool-test-org", organisation.getOrganisationId());
		assertEquals("company name another-test-org", organisation.getName());
	}

	@Test
	public void testFindAllPoolOrganizations() {
		List<Organisation> organizations = subject.findByPoolSizeNotNull();

		assertNotNull(organizations);
		assertEquals(1, organizations.size());

		Organisation organization = organizations.get(0);
		assertEquals("company name another-test-org", organization.getName());
		assertEquals("pool-test-org", organization.getOrganisationId());
		assertEquals(10, organization.getPoolSize().intValue());
	}
}