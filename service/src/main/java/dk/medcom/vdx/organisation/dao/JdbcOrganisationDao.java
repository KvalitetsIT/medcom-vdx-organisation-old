package dk.medcom.vdx.organisation.dao;

import dk.medcom.vdx.organisation.dao.entity.Organisation;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class JdbcOrganisationDao implements OrganisationDao {
    private DataSource dataSource;

    public JdbcOrganisationDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Organisation findByOrganisationId(String organisationId) {
        try {
            var sql = "select * from organisation where organisation_id = :id";

            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
            return template.queryForObject(sql, new MapSqlParameterSource("id", organisationId), this::mapResult);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Organisation> findByPoolSizeNotNull() {
        try {
            var sql = "select * from organisation where pool_size is not null";

            var template = new NamedParameterJdbcTemplate(dataSource);
            return template.query(sql, this::mapResult);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Organisation mapResult(ResultSet resultSet, int i) throws SQLException {
        var organisation = new Organisation();
        organisation.setPoolSize(resultSet.getInt("pool_size"));
        if(resultSet.wasNull()) {
            organisation.setPoolSize(null);
        }
        organisation.setOrganisationId(resultSet.getString("organisation_id"));
        organisation.setName(resultSet.getString("name"));
        organisation.setId(resultSet.getLong("id"));

        return organisation;
    }

}
