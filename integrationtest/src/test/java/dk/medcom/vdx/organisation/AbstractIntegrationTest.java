package dk.medcom.vdx.organisation;

import org.openapitools.client.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Base64;

public class AbstractIntegrationTest {
	private static final Logger logger = LoggerFactory.getLogger(AbstractIntegrationTest.class);
	private static final Logger mysqlLogger = LoggerFactory.getLogger("mysql");
	private static final Logger organisationLogger = LoggerFactory.getLogger("organisation");

	public static final String TEST_AUTH_HEADER = "X-Test-Auth";
	public static final String TEST_USER_ATTRIBUTES_ROLE_KEY = "UserRoles";
	public static final String TEST_USER_ATTRIBUTES_ORG_KEY = "Org";
	public static final String TEST_ROLE_ADMIN = "admin";
	public static final String TEST_ROLE_USER_1 = "justuser";
	public static final String TEST_ROLE_USER_2 = "user";
	public static final String TEST_ROLE_MONITOR = "monitor";
	public static final String TEST_ROLE_PROVISIONER = "provisioner";
	
	public static final String TEST_ORGANISATION_A = "org-a";
	public static final String TEST_ORGANISATION_B = "org-b";
	
	
	private static Network dockerNetwork;
	
	private static String apiBasePath;
	private static GenericContainer organisationService;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				if(organisationService != null) {
					logger.info("Stopping organisation container: " + organisationService.getContainerId());
					organisationService.getDockerClient().stopContainerCmd(organisationService.getContainerId()).exec();
				}
			}
		});

		setup();
	}

	public static void setup() {
		var runInDocker = Boolean.getBoolean("runInDocker");
		logger.info("Running integration test in docker continer: " + runInDocker);

		if (dockerNetwork == null) {
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

			if(runInDocker) {
				// OrganisationsAPI
				organisationService = new GenericContainer<>("local/medcom-vdx-organisation-qa:dev")
						.withNetwork(dockerNetwork)
						.withNetworkAliases("organisation")
						.withEnv("jdbc_url", "jdbc:mysql://mysql:3306/organisationdb")
						.withEnv("jdbc_user", "orguser")
						.withEnv("jdbc_pass", "secret1234")

						.withEnv("usercontext_header_name", TEST_AUTH_HEADER)
						.withEnv("userattributes_role_key", TEST_USER_ATTRIBUTES_ROLE_KEY)
						.withEnv("userrole_admin_values", TEST_ROLE_ADMIN)
						.withEnv("userrole_user_values", TEST_ROLE_USER_1+","+TEST_ROLE_USER_2)
						.withEnv("userrole_monitor_values", TEST_ROLE_MONITOR)
						.withEnv("userrole_provisioner_values", TEST_ROLE_PROVISIONER)
						.withEnv("userattributes_org_key", TEST_USER_ATTRIBUTES_ORG_KEY)

						// Contains integrationtestdata
						.withEnv("LOADER_PATH", "/app/lib")
						.withEnv("JVM_OPTS", "-cp integrationtest.jar")

						.withEnv("LOG_LEVEL", "DEBUG")

						// Jacoco for code coverage of integration test.
						.withFileSystemBind("/tmp", "/jacoco-report/")
						.withEnv("JVM_OPTS", "-javaagent:/jacoco/jacocoagent.jar=output=file,destfile=/jacoco-report/jacoco-it.exec,dumponexit=true")

						.withExposedPorts(8080)
						.waitingFor(Wait.forHttp("/temp").forStatusCode(404)); //TODO: Bruge info-url
				organisationService.start();
				attachLogger(organisationService, organisationLogger);

				apiBasePath = "http://"+organisationService.getContainerIpAddress()+":"+organisationService.getMappedPort(8080);
			}
			else {
				String jdbcUrl = mysql.getJdbcUrl();
				System.setProperty("jdbc.url", jdbcUrl);
				SpringApplication.run(Application.class, new String[]{});
				apiBasePath = "http://localhost:8080";
			}
		}
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

	protected String getEncodedUserContext(String[] roles, String org) {
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
		return Base64.getEncoder().encodeToString(sessionDataBytes);
	}

	protected void setUserContext(ApiClient apiClient, String[] roles, String org) {
		apiClient.addDefaultHeader(TEST_AUTH_HEADER, getEncodedUserContext(roles, org));
	}
}