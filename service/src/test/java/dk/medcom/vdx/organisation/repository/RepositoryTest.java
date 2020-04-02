package dk.medcom.vdx.organisation.repository;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

import dk.medcom.vdx.organisation.configuration.DatabaseConfiguration;
import dk.medcom.vdx.organisation.configuration.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("test.properties")
@ContextConfiguration(
  classes = { TestConfiguration.class, DatabaseConfiguration.class }, 
  loader = AnnotationConfigContextLoader.class)
@Transactional
abstract public class RepositoryTest {

	private static Object initialized = null;
	
	@BeforeClass
	public static void setupMySqlJdbcUrl() {
		
		if (initialized == null) {
			MySQLContainer mysql = (MySQLContainer) new MySQLContainer("mysql:5.7")
					.withDatabaseName("organisationdb")
					.withUsername("orguser")
					.withPassword("secret1234");
			mysql.start();

			String jdbcUrl = mysql.getJdbcUrl();
			System.setProperty("jdbc.url", jdbcUrl);
			
			initialized = new Object();
		}
	}
	
}
