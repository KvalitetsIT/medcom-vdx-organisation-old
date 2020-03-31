package dk.medcom.vdx.organisation.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.dao.rowmappers.OrganisationRowMapper;

@Component
public class JdbcOrganisationDao implements OrganisationDao {

	private DataSource dataSource;

	private OrganisationRowMapper organisationRowMapper = new OrganisationRowMapper();

	public JdbcOrganisationDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Organisation findByOrganisationShortName(String organisationShortName) {
		try {
			var sql = "select * from organisation where organisation_id = :shortName";
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
			return template.queryForObject(sql, new MapSqlParameterSource("shortName", organisationShortName), organisationRowMapper);
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Organisation> findOrganisations(String organisationShortName) {
		var sql = "select * from organisation where organisation_id = :shortName";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, new MapSqlParameterSource("shortName", organisationShortName), organisationRowMapper);
	}

	@Override
	public List<Organisation> findOrganisations() {
		var sql = "select * from organisation";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, organisationRowMapper);
	}

	@Override
	public List<Organisation> findByPoolSizeNotNull() {
		var sql = "select * from organisation where pool_size is not null";
		var template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, organisationRowMapper);
	}

	@Override
	public Organisation updateOrganisationWithShortName(String organisationShortName, String organisationName, int poolSize) {
		var sql = "update organisation set name = :name, pool_size = :poolSize where organisation_id = :shortName";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("shortName", organisationShortName);
		parameterMap.put("poolSize", poolSize);
		parameterMap.put("name", organisationName);
		template.update(sql, new MapSqlParameterSource(parameterMap));
		return findByOrganisationShortName(organisationShortName);
	}

	@Override
	public Organisation createOrganisation(String organisationShortName, String organisationName, int poolSize) {
		SimpleJdbcInsert template = new SimpleJdbcInsert(dataSource).withTableName("organisation");
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("organisation_id", organisationShortName);
		parameterMap.put("pool_size", poolSize);
		parameterMap.put("name", organisationName);
		template.execute(new MapSqlParameterSource(parameterMap));
		return findByOrganisationShortName(organisationShortName);
	}
}
