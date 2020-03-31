package dk.medcom.vdx.organisation.configuration;

import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.impl.JdbcOrganisationDao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("test.properties")
public class TestConfiguration {
    @Bean
    public OrganisationDao organisationDao(DataSource dataSource) {
        return new JdbcOrganisationDao(dataSource);
    }
}
