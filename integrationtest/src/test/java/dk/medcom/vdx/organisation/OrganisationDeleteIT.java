package dk.medcom.vdx.organisation;

import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OrganisationApi;
import org.openapitools.client.model.Organisation;

public class OrganisationDeleteIT extends AbstractIntegrationTest {


	OrganisationApi subject;

	ApiClient apiClient;

	@Before
	public void setupTest() {
		apiClient = new ApiClient();
		apiClient.setBasePath(getApiBasePath());
		subject = new OrganisationApi(apiClient);
	}

	@Test(expected = ApiException.class)
	public void testThatAdminCanDeleteOrganisation() throws ApiException {

		// Given
		String code = "org"+UUID.randomUUID();
		setUserContext(apiClient, new String[]{ TEST_ROLE_ADMIN }, code);
		Organisation organisation = new Organisation();
		organisation.setCode(code);
		organisation.setName("Org");
		organisation.setPoolSize(100);
		Organisation foundBeforeDelete = null;

		try {

			subject.servicesOrganisationPost(organisation);
			foundBeforeDelete = subject.servicesOrganisationCodeGet(code);

			// When
			subject.servicesOrganisationCodeDelete(code);

		} catch (ApiException e) {
			// We dont expect exceptions here
			fail();
		}

		// Then
		Assert.assertNotNull(foundBeforeDelete);
		try {
			foundBeforeDelete = subject.servicesOrganisationCodeGet(code);
		} catch (ApiException e) {

			Assert.assertEquals(HttpCodes.NOT_FOUND, e.getCode());
			throw e;
		}
	}

}
