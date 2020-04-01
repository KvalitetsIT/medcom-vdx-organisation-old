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

public class FindOrganisationServiceImplTest extends RepositoryTest {

	static final String ORG_A_CODE = "orga";
	static final String ORG_B_CODE = "orgb";

	UserContextService userWithNoOrganisationContext;
	UserContextService userFromOrgAContext;
	UserContextService userFromOrgBContext;

	@Autowired
	OrganisationDao organisationDao;

	FindOrganisationServiceImpl subject;
	
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
	
	@Test(expected = PermissionDeniedException.class)
	public void testThatUserFromOrgACannotGetOrgB() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userFromOrgAContext, organisationDao);
		
		// When
		subject.findOrganisationFromCode(ORG_B_CODE);
		
		// Then
	}

	@Test
	public void testThatUserFromOrgACanGetOrgA() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userFromOrgAContext, organisationDao);
		
		// When
		OrganisationDto orgA = subject.findOrganisationFromCode(ORG_A_CODE);
		
		// Then
		Assert.assertNotNull(orgA);
		Assert.assertEquals(ORG_A_CODE, orgA.getCode());
		Assert.assertEquals("Organisationen kaldet æøå&/%", orgA.getName());
	}

	@Test
	public void testThatUserWithNoOrgCanGetAllOrganisations() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userWithNoOrganisationContext, organisationDao);
		
		// When
		OrganisationDto orgA = subject.findOrganisationFromCode(ORG_A_CODE);
		
		// Then
		Assert.assertNotNull(orgA);
		Assert.assertEquals(ORG_A_CODE, orgA.getCode());
		Assert.assertEquals("Organisationen kaldet æøå&/%", orgA.getName());
	}

	@Test
	public void testThatUserFromOrgBFindsOrganisationUnderB() throws PermissionDeniedException, RessourceNotFoundException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userFromOrgAContext, organisationDao);
		
		// When
		List<OrganisationDto> organisations = subject.findOrganisations();
		
		// Then
		Assert.assertNotNull(organisations);
		Assert.assertEquals(1, organisations.size());
	}
	
	@Test
	public void testThatUserWithNoOrgFindsAllOrganisation() throws PermissionDeniedException, RessourceNotFoundException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userWithNoOrganisationContext, organisationDao);
		
		// When
		List<OrganisationDto> organisations = subject.findOrganisations();
		
		// Then
		Assert.assertNotNull(organisations);
		Assert.assertTrue(organisations.size() >= 2);
	}

}
