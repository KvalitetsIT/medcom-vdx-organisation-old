package dk.medcom.vdx.organisation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OrganisationApi;
import org.openapitools.client.model.Organisation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class OrganisationGetIT extends AbstractIntegrationTest {


	OrganisationApi subject;

	ApiClient apiClient;

	@Before
	public void setupTest() {
		apiClient = new ApiClient();
		apiClient.setBasePath(getApiBasePath());
		subject = new OrganisationApi(apiClient);
	}

	@Test(expected = ApiException.class)
	public void testGetOrganisationsFailsWithNoUser() throws ApiException {

		// Given
		try {

			// When
			subject.servicesOrganisationGet();
		} catch (ApiException e) {

			// Then
			Assert.assertEquals(HttpCodes.FORBIDDEN, e.getCode());
			throw e;
		}
	}

	@Test(expected = ApiException.class)
	public void testGetOrganisationsFailsWithUserNotAdminOrUser() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ "unknown-role" });

		try {

			// When
			subject.servicesOrganisationGet();
		} catch (ApiException e) {

			// Then
			Assert.assertEquals(HttpCodes.FORBIDDEN, e.getCode());
			throw e;
		}
	}

	@Test(expected = ApiException.class)
	public void testGetOrganisationsFailsWithUserWithoutOrganisation() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 });

		try {

			// When
			subject.servicesOrganisationGet();
		} catch (ApiException e) {

			// Then
			Assert.assertEquals(HttpCodes.FORBIDDEN, e.getCode());
			throw e;
		}
	}

	@Test
	public void testGetOrganisationsWithUser() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 }, TEST_ORGANISATION_A);

		// When
		List<Organisation> orgs = subject.servicesOrganisationGet();
		
		// Then
		Assert.assertNotNull(orgs);
		Assert.assertEquals(2, orgs.size());
		
		Set<String> codes = new HashSet<String>();
		for (Organisation org : orgs) {
			codes.add(org.getCode());
		}
		
		Assert.assertTrue(codes.contains(TEST_ORGANISATION_A));
		Assert.assertTrue(codes.contains(TEST_ORGANISATION_A_SUB));
	}
	
	@Test
	public void testGetOrganisationFromCodeWithRegularUser() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 }, TEST_ORGANISATION_A);

		// When
		Organisation org = subject.servicesOrganisationCodeGet(TEST_ORGANISATION_A);
		
		// Then
		Assert.assertNotNull(org);
		Assert.assertEquals(org.getCode(), TEST_ORGANISATION_A);
	}

}