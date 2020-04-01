CREATE TABLE org_hierarchy (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  organisation_id bigint NOT NULL,
  parent_org_id bigint NOT NULL,
  distance int(4) NOT null,
  PRIMARY KEY (id),
  FOREIGN KEY (organisation_id) REFERENCES organisation(id),
  FOREIGN KEY (parent_org_id) REFERENCES organisation(id)
)