package dk.medcom.vdx.organisation;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OrganisationApi;
import org.openapitools.client.model.Organisation;

public class OrganisationPostIT extends AbstractIntegrationTest {

	OrganisationApi subject;

	ApiClient apiClient;

	@Before
	public void setupTest() {
		apiClient = new ApiClient();
		apiClient.setBasePath(getApiBasePath());
		subject = new OrganisationApi(apiClient);
	}

	@Test(expected = ApiException.class)
	public void testPostOrganisationsWithUser() throws ApiException {

		// Given
		String shortNameOrg = "org"+UUID.randomUUID();
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 }, shortNameOrg);
		Organisation organisation = new Organisation();
		organisation.setShortName(shortNameOrg);
		organisation.setName("Org");
		organisation.setPoolSize(100);

		// When
		try {
			subject.servicesOrganisationPost(organisation);
		} catch (ApiException e) {

			// Then
			Assert.assertEquals(HttpCodes.FORBIDDEN, e.getCode());
			throw e;
		}
	}
	
	@Test
	public void testPostOrganisationsWithAdmin() throws ApiException {

		// Given
		String shortNameOrg = "org"+UUID.randomUUID();
		setUserContext(apiClient, new String[]{ TEST_ROLE_ADMIN }, shortNameOrg);
		Organisation organisation = new Organisation();
		organisation.setShortName(shortNameOrg);
		organisation.setName("Org");
		organisation.setPoolSize(100);

		// When
		Organisation result = subject.servicesOrganisationPost(organisation);
		
		// Then
		Assert.assertNotNull(result);
		Assert.assertEquals(result.getShortName(), shortNameOrg);
		Assert.assertEquals(result.getName(), organisation.getName());
		Assert.assertEquals(result.getPoolSize(), organisation.getPoolSize());
	}
}
