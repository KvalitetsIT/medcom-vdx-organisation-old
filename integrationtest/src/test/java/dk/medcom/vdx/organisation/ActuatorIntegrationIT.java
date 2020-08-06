package dk.medcom.vdx.organisation;

import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ActuatorIntegrationIT extends AbstractIntegrationTest {
	@Test
	public void testMonitorRoleGivesAccess() {
		// Given
		var userContext = getEncodedUserContext(new String[]{ TEST_ROLE_MONITOR }, TEST_ORGANISATION_A);

		// When
		String result = getClient("/actuator/info")
				.request()
				.header(TEST_AUTH_HEADER, userContext)
				.get(String.class);
		assertNotNull(result);
	}

	@Test
	public void testPrometheusConnection(){
		var path = getClient("/actuator/prometheus");
		var result = path.request().get(String.class);
		assertNotNull(result);
		assertTrue(result.contains("application_information"));
	}


	@Test(expected = ForbiddenException.class)
	@Ignore
	public void testOtherRoleDoesNotGiveAccess() {
		// Given
		var userContext = getEncodedUserContext(new String[]{ TEST_ROLE_USER_1 }, TEST_ORGANISATION_A);

		// When
		getClient("/actuator/info")
				.request()
				.header(TEST_AUTH_HEADER, userContext)
				.get(String.class);
	}

	private WebTarget getClient(String path) {
		WebTarget target = ClientBuilder.newClient()
				.target(UriBuilder.fromUri(getActuatorPrometheusBasePath() + path));

		return target;
	}
}
