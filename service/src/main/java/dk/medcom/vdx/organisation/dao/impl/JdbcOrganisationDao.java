package dk.medcom.vdx.organisation.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import dk.medcom.vdx.organisation.dao.OrganisationDao;
import dk.medcom.vdx.organisation.dao.entity.Organisation;
import dk.medcom.vdx.organisation.dao.rowmappers.OrganisationRowMapper;
import dk.medcom.vdx.organisation.exceptions.DataIntegretyException;

@Repository
public class JdbcOrganisationDao implements OrganisationDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcOrganisationDao.class);

	private static final String NOT_DELETED = "1970-01-01 00:00:01";  // Not deleted - we have a not null constraint on deleted_at
	private final Date NOT_DELETED_DATE;

	private final DataSource dataSource;

	private OrganisationRowMapper organisationRowMapper = new OrganisationRowMapper();

	public JdbcOrganisationDao(DataSource dataSource) {
		this.dataSource = dataSource;
		try {
			NOT_DELETED_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(NOT_DELETED); // the 'datetime' type is always UTC in the database
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Organisation findByOrganisationCode(String organisationCode) {
		try {
			var sql = "select o.*, p.id, p.organisation_id "
					+ "from organisation as o "
					+ "  left join org_hierarchy as h on o.id = h.organisation_id and h.distance = 1 "
					+ "  left join organisation p on h.parent_org_id = p.id "
					+ "    where o.organisation_id = :code"
					+ "      and o.deleted_at = '"+NOT_DELETED+"'";
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
				+ "where s.organisation_id = :code "
				+ "  and o.deleted_at = '"+NOT_DELETED+"'";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, new MapSqlParameterSource("code", organisationCode), organisationRowMapper);
	}

	@Override
	public List<Organisation> findOrganisations() {
		var sql = "select o.*, p.id, p.organisation_id "
				+ "from organisation as o "
				+ "  left join org_hierarchy as h on o.id = h.organisation_id and h.distance = 1 "
				+ "  left join organisation p on h.parent_org_id = p.id "
				+ "where o.deleted_at = '"+NOT_DELETED+"'";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, organisationRowMapper);
	}

	@Override
	public List<Organisation> findByPoolSizeGreaterThanZero() {
		var sql = "select o.*, p.id, p.organisation_id "
				+ "from organisation as o "
				+ "  left join org_hierarchy as h on o.id = h.organisation_id and h.distance = 1 "
				+ "  left join organisation p on h.parent_org_id = p.id "
				+ "where o.pool_size > 0 "
				+ "  and o.deleted_at = '"+NOT_DELETED+"'"; // Not deleted
		var template = new NamedParameterJdbcTemplate(dataSource);
		return template.query(sql, organisationRowMapper);
	}

	@Override
	public Organisation updateOrganisationWithCode(String organisationCode, String organisationName, int poolSize, List<Organisation> newAncestorsOrderedByDistanceClosestFirst, List<Organisation> oldAncestorsOrderedByDistanceClosestFirst) {

		Organisation  toUpdate = findByOrganisationCode(organisationCode);
		var sql = "update organisation set name = :name, pool_size = :poolSize where id = :id";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("id", toUpdate.getId());
		parameterMap.put("poolSize", poolSize);
		parameterMap.put("name", organisationName);
		template.update(sql, new MapSqlParameterSource(parameterMap));

		// Delete links to old ancestors from the node to update and the nodes below
		if (oldAncestorsOrderedByDistanceClosestFirst != null && oldAncestorsOrderedByDistanceClosestFirst.size() > 0) {

			List<Long> oldAncestorIds = new LinkedList<>();
			for (Organisation oldAncestor : oldAncestorsOrderedByDistanceClosestFirst) {
				oldAncestorIds.add(oldAncestor.getId());
			}

			NamedParameterJdbcTemplate deleteLinksTemplate = new NamedParameterJdbcTemplate(dataSource);
			Map<String, Object> deleteLinksParameterMap = new HashMap<String, Object>();
			deleteLinksParameterMap.put("id", toUpdate.getId());
			deleteLinksParameterMap.put("oldAncestors", oldAncestorIds);
			deleteLinksTemplate.update(
					"delete h from org_hierarchy h"+
					"  join org_hierarchy subs on h.organisation_id = subs.organisation_id and subs.parent_org_id = :id"+
					" where h.parent_org_id in (:oldAncestors)", new MapSqlParameterSource(deleteLinksParameterMap));
		}

		// Insert links to new ancestors for the node to update and the nodes below
		if (newAncestorsOrderedByDistanceClosestFirst != null && newAncestorsOrderedByDistanceClosestFirst.size() > 0) {

			for (int i = 0; i < newAncestorsOrderedByDistanceClosestFirst.size(); i++) {
				Map<String, Object> parameterMapToInsertLinksToNewAncestors = new HashMap<String, Object>();
				parameterMapToInsertLinksToNewAncestors.put("add", i + 1);
				parameterMapToInsertLinksToNewAncestors.put("id", toUpdate.getId());
				parameterMapToInsertLinksToNewAncestors.put("parent", newAncestorsOrderedByDistanceClosestFirst.get(i).getId());
				NamedParameterJdbcTemplate insertLinksTemplate = new NamedParameterJdbcTemplate(dataSource);
				insertLinksTemplate.update("insert into org_hierarchy(organisation_id, parent_org_id, distance) "+
						" select subs.organisation_id, :parent, subs.distance + :add "+
						"   from org_hierarchy as subs "+
			            " where subs.parent_org_id = :id", parameterMapToInsertLinksToNewAncestors);
			}
		}
		return findByOrganisationCode(organisationCode);
	}

	@Override
	public Organisation createOrganisation(List<Organisation> parentOrganisationsOrderedByDistance, String organisationCode, String organisationName, int poolSize) throws DataIntegretyException {
		try {
			SimpleJdbcInsert template = new SimpleJdbcInsert(dataSource).withTableName("organisation").usingGeneratedKeyColumns("id");
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("organisation_id", organisationCode);
			parameterMap.put("pool_size", poolSize);
			parameterMap.put("name", organisationName);
			parameterMap.put("deleted_at", NOT_DELETED_DATE);
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
		} catch (DuplicateKeyException e) {
			String message = "Organisation by code '"+organisationCode+"' already exists";
			LOGGER.debug(message, e);
			DataIntegretyException die = new DataIntegretyException(message);
			throw die;
		}
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
		return template.query(sql, new MapSqlParameterSource("orgId", organisationId), organisationRowMapper);
	}

	@Override
	public void deleteOrganisation(Organisation orgToDelete) {
		
		NamedParameterJdbcTemplate updateOrganisationsTemplate = new NamedParameterJdbcTemplate(dataSource);
		Map<String, Object> updateOrganisationsMap = new HashMap<String, Object>();
		updateOrganisationsMap.put("id", orgToDelete.getId());
		updateOrganisationsTemplate.update(
				"update organisation as o "
				+ "   join org_hierarchy as h on o.id = h.organisation_id"  
				+ "   join organisation as p on p.id = h.parent_org_id "  
				+ "set o.deleted_at = now() " // Delete by setting this datetime
				+ "where p.id = :id", updateOrganisationsMap);
	}
}
