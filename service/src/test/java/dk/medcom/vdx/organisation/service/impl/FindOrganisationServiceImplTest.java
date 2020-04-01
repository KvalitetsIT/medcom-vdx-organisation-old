package dk.medcom.vdx.organisation.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	static final String ORG_A_CODE = "org-a";
	static final String ORG_A_CODE_SUB = "sub-org-a";
	static final String ORG_B_CODE = "org-b";

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
		Assert.assertNull(orgA.getParentCode());
	}

	@Test
	public void testThatUserFromOrgAFindsAllOrganisationsUnderAndIncludingA() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userFromOrgAContext, organisationDao);
		
		// When
		List<OrganisationDto> orgs = subject.findOrganisations();
		
		// Then
		Assert.assertNotNull(orgs);
		Assert.assertEquals(2, orgs.size());
		
		Map<String, OrganisationDto> resultMap = new HashMap<String, OrganisationDto>();
		for (OrganisationDto org : orgs) {
			resultMap.put(org.getCode(), org);
		}
		Assert.assertTrue(resultMap.containsKey(ORG_A_CODE));
		Assert.assertTrue(resultMap.containsKey(ORG_A_CODE_SUB));

		OrganisationDto orgA = resultMap.get(ORG_A_CODE);
		Assert.assertEquals(ORG_A_CODE, orgA.getCode());
		Assert.assertEquals("Organisationen kaldet æøå&/%", orgA.getName());
		Assert.assertNull(orgA.getParentCode());
		
		OrganisationDto subOrg = resultMap.get(ORG_A_CODE_SUB);
		Assert.assertEquals(ORG_A_CODE_SUB, subOrg.getCode());
		Assert.assertEquals(orgA.getCode(), subOrg.getParentCode());
	}

	@Test
	public void testThatUserWithNoOrgCanGetOrganisationA() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
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
	public void testThatUserWithNoOrgCanGetOrganisationSubA() throws PermissionDeniedException, RessourceNotFoundException, BadRequestException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userWithNoOrganisationContext, organisationDao);
		
		// When
		OrganisationDto subOrgA = subject.findOrganisationFromCode(ORG_A_CODE_SUB);
		
		// Then
		Assert.assertNotNull(subOrgA);
		Assert.assertEquals(ORG_A_CODE_SUB, subOrgA.getCode());
		Assert.assertEquals(ORG_A_CODE, subOrgA.getParentCode());
	}

	@Test
	public void testThatUserFromOrgBFindsOrganisationUnderB() throws PermissionDeniedException, RessourceNotFoundException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userFromOrgBContext, organisationDao);
		
		// When
		List<OrganisationDto> organisations = subject.findOrganisations();
		
		// Then
		Assert.assertNotNull(organisations);
		Assert.assertEquals(1, organisations.size());
		Assert.assertEquals(ORG_B_CODE, organisations.get(0).getCode());
		Assert.assertNull(organisations.get(0).getParentCode());
	}
	
	@Test
	public void testThatUserWithNoOrgFindsAllOrganisation() throws PermissionDeniedException, RessourceNotFoundException {
		
		// Given
		subject = new FindOrganisationServiceImpl(userWithNoOrganisationContext, organisationDao);
		
		// When
		List<OrganisationDto> organisations = subject.findOrganisations();
		
		// Then
		Assert.assertNotNull(organisations);
		Assert.assertTrue(organisations.size() == 10);
	}
}
