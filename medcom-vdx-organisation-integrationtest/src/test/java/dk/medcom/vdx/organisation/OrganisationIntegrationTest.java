package dk.medcom.vdx.organisation;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OrganisationApi;
import org.openapitools.client.model.Organisation;

public class OrganisationIntegrationTest extends AbstractIntegrationTest {

	
	OrganisationApi subject;
	
	@Before
	public void setupTest() {
		ApiClient apiClient = getApiClient();
		subject = new OrganisationApi(apiClient);
	}
	
	@Test
	public void testGetOrganisations() throws ApiException {
		
		// Given
		
		// When
		List<Organisation> orgs = subject.servicesOrganisationGet();
		
		// Then
		
		
	}
}
