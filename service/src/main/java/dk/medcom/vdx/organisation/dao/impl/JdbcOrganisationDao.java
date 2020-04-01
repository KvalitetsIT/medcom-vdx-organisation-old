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
	public Organisation findByOrganisationCode(String organisationCode) {
		try {
			var sql = "select o.*, p.id, p.organisation_id "
					+ "from organisation as o "
					+ "  left join org_hierarchy as h on o.id = h.organisation_id and h.distance = 1 "
					+ "  left join organisation p on h.parent_org_id = p.id "
					+ "    where o.organisation_id = :code";
	//		var sql = "select o.* from organisation as o where o.organisation_id = :code";
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
			return template.queryForObject(sql, new MapSqlParameterSource("code", organisationCode), organisationRowMapper);
		}
		catch(EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Organisation> findOrganisations(String organisationCode) {
		var sql = "select o.*, p.id, p.organisation_id "
				+ "from organisation as s " 
				+ "   join org_hierarchy as h on s.id = h.parent_org_id"  
				+ "   join organisation as o on o.id = h.organisation_id"  
				+ "   left join org_hierarchy as h2 on o.id = h2.organisation_id and h2.distance = 1"
				+ "   left join organisation p on h2.parent_org_id = p.id "
				+ "where s.organisation_id = :code";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, new MapSqlParameterSource("code", organisationCode), organisationRowMapper);
	}

	@Override
	public List<Organisation> findOrganisations() {
		var sql = "select o.*, p.id, p.organisation_id "
				+ "from organisation as o "
				+ "  left join org_hierarchy as h on o.id = h.organisation_id and h.distance = 1 "
				+ "  left join organisation p on h.parent_org_id = p.id";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, organisationRowMapper);
	}

	@Override
	public List<Organisation> findByPoolSizeGreaterThanZero() {
		var sql = "select o.*, p.id, p.organisation_id "
				+ "from organisation as o "
				+ "  left join org_hierarchy as h on o.id = h.organisation_id and h.distance = 1 "
				+ "  left join organisation p on h.parent_org_id = p.id "
				+ "where o.pool_size > 0";
		var template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, organisationRowMapper);
	}

	@Override
	public Organisation updateOrganisationWithCode(String organisationCode, String organisationName, int poolSize, List<Organisation> newAncestorsOrderedByDistanceClosestFirst) {
		var sql = "update organisation set name = :name, pool_size = :poolSize where organisation_id = :code";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("code", organisationCode);
		parameterMap.put("poolSize", poolSize);
		parameterMap.put("name", organisationName);
		template.update(sql, new MapSqlParameterSource(parameterMap));
		
		//TODO: handle List<Organisation> newAncestorsOrderedByDistanceClosestFirst
		
		return findByOrganisationCode(organisationCode);
	}

	@Override
	public Organisation createOrganisation(List<Organisation> parentOrganisationsOrderedByDistance, String organisationCode, String organisationName, int poolSize) {
		SimpleJdbcInsert template = new SimpleJdbcInsert(dataSource).withTableName("organisation").usingGeneratedKeyColumns("id");
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("organisation_id", organisationCode);
		parameterMap.put("pool_size", poolSize);
		parameterMap.put("name", organisationName);
		Number createdId = template.executeAndReturnKey(new MapSqlParameterSource(parameterMap));

		// Create the hierarchy (point to oneself and to all parents)
		SimpleJdbcInsert insertHierarchyPointToMyself = new SimpleJdbcInsert(dataSource).withTableName("org_hierarchy");
		Map<String, Object> insertHierarchyMyselfparameterMap = new HashMap<String, Object>();
		insertHierarchyMyselfparameterMap.put("organisation_id", createdId.longValue());
		insertHierarchyMyselfparameterMap.put("parent_org_id", createdId.longValue());
		insertHierarchyMyselfparameterMap.put("distance", 0);
		insertHierarchyPointToMyself.execute(new MapSqlParameterSource(insertHierarchyMyselfparameterMap));
		
		if (parentOrganisationsOrderedByDistance != null) {
			for (int i = 0; i < parentOrganisationsOrderedByDistance.size(); i++) {
				SimpleJdbcInsert insertHierarchyPointToAncestor = new SimpleJdbcInsert(dataSource).withTableName("org_hierarchy");
				Map<String, Object> insertHierarchyPointToAncestorParameterMap = new HashMap<String, Object>();
				insertHierarchyPointToAncestorParameterMap.put("organisation_id", createdId.longValue());
				insertHierarchyPointToAncestorParameterMap.put("parent_org_id", parentOrganisationsOrderedByDistance.get(i).getId());
				insertHierarchyPointToAncestorParameterMap.put("distance", i+1);
				insertHierarchyPointToAncestor.execute(new MapSqlParameterSource(insertHierarchyPointToAncestorParameterMap));
			}
		}
		
		Organisation created = findByOrganisationCode(organisationCode);
		return created;
	}

	@Override
	public List<Organisation> findAncestorsOrderedByDistanceClosestFirst(Long organisationId) {
		var sql = "select o.*, p.id, p.organisation_id "
				+ "from organisation o "
				+ "  join org_hierarchy as h on o.id = h.parent_org_id"
				+ "   left join org_hierarchy as h2 on o.id = h2.organisation_id and h2.distance = 1"
				+ "   left join organisation p on h2.parent_org_id = p.id "
				+ "where h.organisation_id = :orgId "
				+ "  order by h.distance asc";
		var template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, new MapSqlParameterSource("orgId", organisationId),  organisationRowMapper);
	}
}
