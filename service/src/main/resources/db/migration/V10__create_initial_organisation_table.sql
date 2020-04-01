CREATE TABLE organisation (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  organisation_id varchar(50) NOT NULL,
  pool_size int(4) not null default 0,
  name varchar(100),
  PRIMARY KEY (id),
  UNIQUE KEY (organisation_id)
) 