# ELI5 SCHEMA

# --- !Ups
CREATE TABLE Users (
	id                   			UUID UNIQUE        	NOT NULL,
	role                 			CHARACTER VARYING 	NOT NULL,
	provider_id          			CHARACTER VARYING 	NOT NULL,
	provider_key         			CHARACTER VARYING 	NOT NULL,
	password             			CHARACTER VARYING 	NOT NULL,
	full_name                 		CHARACTER VARYING 	NOT NULL,
	reset_password_token 			CHARACTER VARYING 		NULL,

	PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE Users;
