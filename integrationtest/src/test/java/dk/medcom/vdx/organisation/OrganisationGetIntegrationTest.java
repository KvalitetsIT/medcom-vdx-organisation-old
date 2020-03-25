package dk.medcom.vdx.organisation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OrganisationApi;


public class OrganisationGetIntegrationTest extends AbstractIntegrationTest {


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
			Assert.assertEquals("Expected unauthorized response", 401, e.getCode());
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
			Assert.assertEquals("Expected unauthorized response", 401, e.getCode());
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
			Assert.assertEquals("Expected forbidden response", 403, e.getCode());
			throw e;
		}
	}

	@Test
	public void testGetOrganisationsWithUser() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 }, TEST_ORGANISATION_A);

		// When
		subject.servicesOrganisationGet();
		
		// Then
	}
}
