package dk.medcom.vdx.organisation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.OrganisationApi;
import org.openapitools.client.model.Organisationtree;


public class OrganisationTreeGetIT extends AbstractIntegrationTest {


	OrganisationApi subject;

	ApiClient apiClient;

	@Before
	public void setupTest() {
		apiClient = new ApiClient();
		apiClient.setBasePath(getApiBasePath());
		subject = new OrganisationApi(apiClient);
	}

	@Test(expected = ApiException.class)
	public void testGetOrganisationTreeFromCodeFailsWithNoUser() throws ApiException {

		// Given
		try {

			// When
			subject.servicesOrganisationtreeCodeGet(TEST_ORGANISATION_A);
		} catch (ApiException e) {

			// Then
			Assert.assertEquals(HttpCodes.FORBIDDEN, e.getCode());
			throw e;
		}
	}

	@Test
	public void testGetOrganisationTreeFromCodeWithUserFromThatOrg() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 }, TEST_ORGANISATION_A);

		// When
		Organisationtree treeA = subject.servicesOrganisationtreeCodeGet(TEST_ORGANISATION_A);
		
		// Then
		Assert.assertNotNull(treeA);
		Assert.assertEquals(TEST_ORGANISATION_A, treeA.getCode());
		Assert.assertNotNull(treeA.getChildren());
		Assert.assertEquals(1, treeA.getChildren().size());
		Organisationtree child = treeA.getChildren().get(0);
		Assert.assertEquals(TEST_ORGANISATION_A_SUB, child.getCode());
		Assert.assertNull(child.getChildren());
	}
	

	@Test
	public void testGetOrganisationSubTreeFromCodeWithUserFromThatOrg() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_USER_1 }, TEST_ORGANISATION_A);

		// When
		Organisationtree subtreeA = subject.servicesOrganisationtreeCodeGet(TEST_ORGANISATION_A_SUB);
		
		// Then
		Assert.assertNotNull(subtreeA);
		Assert.assertEquals(TEST_ORGANISATION_A_SUB, subtreeA.getCode());
		Assert.assertNull(subtreeA.getChildren());
	}
	
	
	@Test
	public void testGetOrganisationTreesWithUserWithNoOrg() throws ApiException {

		// Given
		setUserContext(apiClient, new String[]{ TEST_ROLE_PROVISIONER });

		// When
		List<Organisationtree> trees = subject.servicesOrganisationtreeGet();
		
		// Then
		Assert.assertNotNull(trees);
		Assert.assertTrue(trees.size() >= 2);
		Map<String, Organisationtree> result = new HashMap<String, Organisationtree>();
		for (Organisationtree tree : trees) {
			result.put(tree.getCode(), tree);
		}
		Assert.assertTrue(result.containsKey(TEST_ORGANISATION_A));
		Assert.assertTrue(result.containsKey(TEST_ORGANISATION_B));
		Organisationtree treeA = result.get(TEST_ORGANISATION_A);
		Assert.assertNotNull(treeA);
		Assert.assertEquals(TEST_ORGANISATION_A, treeA.getCode());
		Assert.assertNotNull(treeA.getChildren());
		Assert.assertEquals(1, treeA.getChildren().size());
		Organisationtree child = treeA.getChildren().get(0);
		Assert.assertEquals(TEST_ORGANISATION_A_SUB, child.getCode());
		Assert.assertNull(child.getChildren());
	}
}