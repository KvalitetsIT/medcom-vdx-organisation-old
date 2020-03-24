package dk.medcom.vdx.organisation;

import java.util.Base64;

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

	public static final String TEST_AUTH_HEADER = "X-Test-Auth";
	public static final String TEST_USER_ATTRIBUTES_ROLE_KEY = "UserRoles";
	public static final String TEST_USER_ATTRIBUTES_ORG_KEY = "Org";
	public static final String TEST_ROLE_ADMIN = "admin";
	public static final String TEST_ROLE_USER_1 = "justuser";
	public static final String TEST_ROLE_USER_2 = "user";
	
	public static final String TEST_ORGANISATION_A = "org-a";
	public static final String TEST_ORGANISATION_B = "org-b";
	
	
	private static Network dockerNetwork;
	
	private static String apiBasePath;

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

		// OrganisationsAPI
		GenericContainer organisationService = new GenericContainer<>("kvalitetsit/medcom-vdx-organisation-web:dev")
				.withNetwork(dockerNetwork)
				.withNetworkAliases("organisation")
				.withEnv("jdbc_url", "jdbc:mysql://mysql:3306/organisationdb")
				.withEnv("jdbc_user", "orguser")
				.withEnv("jdbc_pass", "secret1234")

				.withEnv("usercontext_header_name", TEST_AUTH_HEADER)
				.withEnv("userattributes_role_key", TEST_USER_ATTRIBUTES_ROLE_KEY)
				.withEnv("userrole_admin_values", TEST_ROLE_ADMIN)
				.withEnv("userrole_user_values", TEST_ROLE_USER_1+","+TEST_ROLE_USER_2)
				.withEnv("userattributes_org_key", TEST_USER_ATTRIBUTES_ORG_KEY)

				.withExposedPorts(8080)
				.waitingFor(Wait.forHttp("/temp").forStatusCode(200)); //TODO: Bruge info-url
		organisationService.start();
		attachLogger(organisationService, organisationLogger);

		apiBasePath = "http://"+organisationService.getContainerIpAddress()+":"+organisationService.getMappedPort(8080);
		
	}

	private static void attachLogger(GenericContainer container, Logger logger) {
		logger.info("Attaching logger to container: " + container.getContainerInfo().getName());
		Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
		container.followOutput(logConsumer);
	}

	protected String getApiBasePath() {
		return apiBasePath;
	}

	protected void setUserContext(ApiClient apiClient, String[] roles) {
		setUserContext(apiClient, roles, null);
	}
	
	protected void setUserContext(ApiClient apiClient, String[] roles, String org) {
		
		StringBuffer sessionData = new StringBuffer();
		sessionData.append("{");
			sessionData.append("\"UserAttributes\": { ");
			boolean firstAttr = true;
				if (roles != null && roles.length > 0) {
					firstAttr = false;
					sessionData.append("\""+TEST_USER_ATTRIBUTES_ROLE_KEY+"\": [");
						boolean firstRole = true;
						for (String role : roles) {
							sessionData.append("\""+role+"\"");
							if (!firstRole) {
								sessionData.append(",");
							}
							firstRole = false;
						}
					sessionData.append("]");
				}
				if (org != null) {
					if (!firstAttr) {
						sessionData.append(",");
					}
					sessionData.append("\""+TEST_USER_ATTRIBUTES_ORG_KEY+"\": [\""+org+"\"]");
					firstAttr = false;
				}
			sessionData.append("}");
		sessionData.append("}");
		byte[] sessionDataBytes = sessionData.toString().getBytes();
		String value = Base64.getEncoder().encodeToString(sessionDataBytes);
		apiClient.addDefaultHeader(TEST_AUTH_HEADER, value);		
	}
}