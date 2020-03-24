package dk.medcom.vdx.organisation.repository;

import javax.transaction.Transactional;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import dk.medcom.vdx.organisation.configuration.DatabaseConfiguration;
import dk.medcom.vdx.organisation.configuration.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("test.properties")
@ContextConfiguration(
  classes = { TestConfiguration.class, DatabaseConfiguration.class }, 
  loader = AnnotationConfigContextLoader.class)
@Transactional
abstract public class RepositoryTest {

	@BeforeClass
	public static void setupMySqlJdbcUrl() {
		MySQLContainer mysql = (MySQLContainer) new MySQLContainer("mysql:5.7").withDatabaseName("organisationdb").withUsername("orguser").withPassword("secret1234");
		mysql.start();

		String jdbcUrl = mysql.getJdbcUrl();
		System.setProperty("jdbc.url", jdbcUrl);
	}
	
}
