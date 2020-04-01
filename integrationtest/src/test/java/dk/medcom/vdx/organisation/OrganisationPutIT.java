package dk.medcom.vdx.organisation;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OrganisationApi;
import org.openapitools.client.model.Organisation;

public class OrganisationPutIT extends AbstractIntegrationTest {

	OrganisationApi subject;

	ApiClient apiClient;

	@Before
	public void setupTest() {
		apiClient = new ApiClient();
		apiClient.setBasePath(getApiBasePath());
		subject = new OrganisationApi(apiClient);
	}

	@Test(expected = ApiException.class)
	public void testPutOrganisationsWithRegularUser() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 }, TEST_ORGANISATION_A);
		Organisation organisation = new Organisation();
		organisation.setCode(TEST_ORGANISATION_A);
		organisation.setName("Org ny titel");
		organisation.setPoolSize(10000);

		// When
		try {
			subject.servicesOrganisationPut(organisation);
		} catch (ApiException e) {

			// Then
			Assert.assertEquals("Regular users should get a forbidden whent PUT'ing Organisations", HttpCodes.FORBIDDEN, e.getCode());
			throw e;
		}
	}
	
	@Test
	public void testPutOrganisationsWithAdmin() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_ADMIN }, TEST_ORGANISATION_A);
		Organisation organisation = new Organisation();
		organisation.setCode(TEST_ORGANISATION_A);
		organisation.setName("Org "+UUID.randomUUID());
		organisation.setPoolSize(100);

		// When
		Organisation result = subject.servicesOrganisationPut(organisation);
		
		// Then
		Assert.assertNotNull(result);
		Assert.assertEquals(result.getCode(), TEST_ORGANISATION_A);
		Assert.assertEquals(result.getName(), organisation.getName());
		Assert.assertEquals(result.getPoolSize(), organisation.getPoolSize());
	}

	@Test
	public void testPutOrganisationsWithProvisioner() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_PROVISIONER }, null);
		Organisation organisation = new Organisation();
		organisation.setCode(TEST_ORGANISATION_A);
		organisation.setName("Prov "+UUID.randomUUID());
		organisation.setPoolSize(200);

		// When
		Organisation result = subject.servicesOrganisationPut(organisation);
		
		// Then
		Assert.assertNotNull(result);
		Assert.assertEquals(result.getCode(), TEST_ORGANISATION_A);
		Assert.assertEquals(result.getName(), organisation.getName());
		Assert.assertEquals(result.getPoolSize(), organisation.getPoolSize());
	}

}
