package dk.medcom.vdx.organisation;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OrganisationApi;
import org.openapitools.client.model.Organisation;


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
		Assert.assertEquals(1, orgs.size());
		Assert.assertEquals(orgs.get(0).getShortName(), TEST_ORGANISATION_A);
	}
	
	@Test
	public void testGetOrganisationFromShortWithUser() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 }, TEST_ORGANISATION_A);

		// When
		Organisation org = subject.servicesOrganisationShortNameGet(TEST_ORGANISATION_A);
		
		// Then
		Assert.assertNotNull(org);
		Assert.assertEquals(org.getShortName(), TEST_ORGANISATION_A);
	}
}