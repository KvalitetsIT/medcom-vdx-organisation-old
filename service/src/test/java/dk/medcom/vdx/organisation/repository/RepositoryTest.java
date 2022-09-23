package dk.medcom.vdx.organisation.repository;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import dk.medcom.vdx.organisation.configuration.DatabaseConfiguration;
import dk.medcom.vdx.organisation.configuration.TestConfiguration;
import org.testcontainers.containers.MariaDBContainer;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("test.properties")
@ContextConfiguration(
  classes = { TestConfiguration.class, DatabaseConfiguration.class }, 
  loader = AnnotationConfigContextLoader.class)
@Transactional
abstract public class RepositoryTest {

	private static Object initialized = null;
	
	@BeforeClass
	public static void setupMariaDbJdbcUrl() {
		if (initialized == null) {
			MariaDBContainer mariaDb = new MariaDBContainer<>("mariadb:10.6")
					.withDatabaseName("organisationdb")
					.withUsername("orguser")
					.withPassword("secret1234");
			mariaDb.start();

			String jdbcUrl = mariaDb.getJdbcUrl();
			System.setProperty("jdbc.url", jdbcUrl);
			
			initialized = new Object();
		}
	}
}
