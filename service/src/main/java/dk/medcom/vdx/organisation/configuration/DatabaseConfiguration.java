package dk.medcom.vdx.organisation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {
	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${jdbc.user}")
	private String jdbcUser;

	@Value("${jdbc.pass}")
	private String jdbcPass;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(jdbcUser);
		dataSource.setPassword(jdbcPass);

		return dataSource;
	}
}
