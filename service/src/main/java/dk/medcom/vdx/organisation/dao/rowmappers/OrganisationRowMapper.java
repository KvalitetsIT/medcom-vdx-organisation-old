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
        organisation.setId(resultSet.getLong("id"));
        organisation.setOrganisationId(resultSet.getString("organisation_id"));
        organisation.setName(resultSet.getString("name"));
        organisation.setPoolSize(resultSet.getInt("pool_size"));
        return organisation;
	}

}
