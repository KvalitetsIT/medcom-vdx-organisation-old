CREATE TABLE organisation (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  organisation_id varchar(50) NOT NULL,
  pool_size int(4) not null default 0,
  name varchar(100),
  deleted_at DATETIME not null default '1970-01-01 00:00:01',
  PRIMARY KEY (id),
  UNIQUE KEY (organisation_id, deleted_at)
) 
