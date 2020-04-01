package dk.medcom.vdx.organisation.dao.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import dk.medcom.vdx.organisation.dao.entity.Organisation;

public class OrganisationRowMapper implements RowMapper<Organisation>{

	@Override
	public Organisation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        if(resultSet.wasNull()) {
        	return null;
        }
        var organisation = new Organisation();
        organisation.setId(resultSet.getLong("o.id"));
        organisation.setOrganisationId(resultSet.getString("o.organisation_id"));
        organisation.setName(resultSet.getString("o.name"));
        organisation.setPoolSize(resultSet.getInt("o.pool_size"));
        organisation.setParentOrganisationId(resultSet.getObject("p.id", Long.class));
        organisation.setParentOrganisationCode(resultSet.getString("p.organisation_id"));
        return organisation;
	}

}
