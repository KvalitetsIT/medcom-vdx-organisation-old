package dk.medcom.vdx.organisation.service.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dk.medcom.vdx.organisation.api.OrganisationDto;
import dk.medcom.vdx.organisation.context.UserContextService;
import dk.medcom.vdx.organisation.context.UserRole;
import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.exceptions.BadRequestException;
import dk.medcom.vdx.organisation.exceptions.PermissionDeniedException;
import dk.medcom.vdx.organisation.exceptions.RessourceNotFoundException;
import dk.medcom.vdx.organisation.repository.RepositoryTest;

public class CreateOrUpdateOrganisationServiceImplTest  extends RepositoryTest {

	static final String ORG_A_CODE = "org-a";
	static final String ORG_B_CODE = "org-b";

	UserContextService userWithNoOrganisationContext;
	UserContextService userFromOrgAContext;
	UserContextService userFromOrgBContext;

	@Autowired
	OrganisationDao organisationDao;

	CreateOrUpdateOrganisationServiceImpl subject;
	
	@Before
	public void setup() {
		
		userFromOrgAContext = new UserContextService() {
			@Override
			public boolean hasAnyNumberOfRoles(List<UserRole> allowed) {
				return true;
			}
			@Override
			public String getOrganisation() {
				return ORG_A_CODE;
			}
		};

		userWithNoOrganisationContext = new UserContextService() {
			@Override
			public boolean hasAnyNumberOfRoles(List<UserRole> allowed) {
				return true;
			}
			@Override
			public String getOrganisation() {
				return null;
			}
		};

		userFromOrgBContext = new UserContextService() {
			@Override
			public boolean hasAnyNumberOfRoles(List<UserRole> allowed) {
				return true;
			}
			@Override
			public String getOrganisation() {
				return ORG_B_CODE;
			}
		};
	}

	@Test
	public void testThatUserWithNoOrgACanCreateNewOrganisation() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		final String NEW_NAME = "Name";
		final String NEW_CODE = "123orgtest87654";
		final int NEW_POOL_SIZE = 100;
		subject = new CreateOrUpdateOrganisationServiceImpl(userWithNoOrganisationContext, organisationDao);
		OrganisationDto toCreate = new OrganisationDto();
		toCreate.setCode(NEW_CODE);
		toCreate.setName(NEW_NAME);
		toCreate.setPoolSize(NEW_POOL_SIZE);

		// When
		OrganisationDto newOrg = subject.createOrganisation(toCreate); 
		
		// Then
		Assert.assertNotNull(newOrg);
		Assert.assertEquals(NEW_CODE, newOrg.getCode());
		Assert.assertEquals(NEW_NAME, newOrg.getName());
		Assert.assertEquals(NEW_POOL_SIZE, newOrg.getPoolSize());
	}

	@Test
	public void testThatUserWithNoOrgCanUpdateA() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		final String NEW_NAME = "New fancy name xyz";
		subject = new CreateOrUpdateOrganisationServiceImpl(userWithNoOrganisationContext, organisationDao);
		OrganisationDto toUpdate = new OrganisationDto();
		toUpdate.setCode(ORG_A_CODE);
		toUpdate.setName(NEW_NAME);
		toUpdate.setPoolSize(200);

		// When
		OrganisationDto newOrgA = subject.updateOrganisation(toUpdate); 
		
		// Then
		Assert.assertNotNull(newOrgA);
		Assert.assertEquals(ORG_A_CODE, newOrgA.getCode());
		Assert.assertEquals(NEW_NAME, newOrgA.getName());
		Assert.assertEquals(200, newOrgA.getPoolSize());
	}

	@Test
	public void testThatUserFromOrgACanUpdateA() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		final String NEW_NAME = "New fancy name";
		subject = new CreateOrUpdateOrganisationServiceImpl(userFromOrgAContext, organisationDao);
		OrganisationDto toUpdate = new OrganisationDto();
		toUpdate.setCode(ORG_A_CODE);
		toUpdate.setName(NEW_NAME);
		toUpdate.setPoolSize(100);

		// When
		OrganisationDto newOrgA = subject.updateOrganisation(toUpdate); 
		
		// Then
		Assert.assertNotNull(newOrgA);
		Assert.assertEquals(ORG_A_CODE, newOrgA.getCode());
		Assert.assertEquals(NEW_NAME, newOrgA.getName());
		Assert.assertEquals(100, newOrgA.getPoolSize());
	}

	@Test(expected = PermissionDeniedException.class)
	public void testThatUserFromOrgACannotUpdateOrgB() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		final String NEW_NAME = "New fancy name";
		subject = new CreateOrUpdateOrganisationServiceImpl(userFromOrgBContext, organisationDao);
		OrganisationDto toUpdate = new OrganisationDto();
		toUpdate.setCode(ORG_A_CODE);
		toUpdate.setName(NEW_NAME);
		toUpdate.setPoolSize(100);
		
		// When
		subject.updateOrganisation(toUpdate);
		
		// Then
	}
}
