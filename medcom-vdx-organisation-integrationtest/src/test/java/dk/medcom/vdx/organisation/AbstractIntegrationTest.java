package dk.medcom.vdx.organisation;

import org.junit.BeforeClass;
import org.openapitools.client.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

public class AbstractIntegrationTest {

	private static final Logger mysqlLogger = LoggerFactory.getLogger("mysql");
	private static final Logger organisationLogger = LoggerFactory.getLogger("organisation");
	
	private static Network dockerNetwork;

	private static ApiClient apiClient = new ApiClient();
	
	@BeforeClass
	public static void setup() {
		dockerNetwork = Network.newNetwork();

		// Database server for Organisation.
		MySQLContainer mysql = (MySQLContainer) new MySQLContainer("mysql:5.7")
				.withDatabaseName("organisationdb")
				.withUsername("orguser")
				.withPassword("secret1234")
				.withNetwork(dockerNetwork)
				.withNetworkAliases("mysql");
		mysql.start();
		attachLogger(mysql, mysqlLogger);

		// Mock server
/*		MockServerContainer userService = new MockServerContainer()
				.withNetwork(dockerNetwork)
				.withNetworkAliases("userservice");
		userService.start();
		attachLogger(userService, mockServerLogger);
		MockServerClient mockServerClient = new MockServerClient(userService.getContainerIpAddress(), userService.getMappedPort(1080));
		mockServerClient.when(HttpRequest.request().withMethod("GET"), Times.unlimited()).respond(getResponse());
*/
		// VideoAPI
		GenericContainer organisationService = new GenericContainer<>("kvalitetsit/medcom-vdx-organisation-web:dev")
				.withNetwork(dockerNetwork)
				.withNetworkAliases("organisation")
				.withEnv("jdbc_url", "jdbc:mysql://mysql:3306/organisationdb")
				.withEnv("jdbc_user", "orguser")
				.withEnv("jdbc_pass", "secret1234")
				.withExposedPorts(8080)
				.waitingFor(Wait.forHttp("/services/organisation").forStatusCode(200)); //TODO: Bruge info-url
		organisationService.start();
		attachLogger(organisationService, organisationLogger);
		

		apiClient = new ApiClient();
		apiClient.setBasePath("http://"+organisationService.getContainerIpAddress()+":"+organisationService.getMappedPort(8080));
		
	}

	private static void attachLogger(GenericContainer container, Logger logger) {
		logger.info("Attaching logger to container: " + container.getContainerInfo().getName());
		Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
		container.followOutput(logConsumer);
	}
	
	protected ApiClient getApiClient() {
		return apiClient;
	}
}
