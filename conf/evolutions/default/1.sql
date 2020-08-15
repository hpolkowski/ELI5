# ELI5 SCHEMA

# --- !Ups
CREATE TABLE Users (
	id                   			UUID      UNIQUE  NOT NULL,
	role                 			CHARACTER VARYING NOT NULL,
	provider_id          			CHARACTER VARYING NOT NULL,
	provider_key         			CHARACTER VARYING NOT NULL,
	password             			CHARACTER VARYING NOT NULL,
	full_name                 		CHARACTER VARYING NOT NULL,
	reset_password_token 			CHARACTER VARYING 	  NULL,

	PRIMARY KEY (id)
);

CREATE TABLE Articles (
	id                   			UUID      UNIQUE  NOT NULL,
	creator_id                   	UUID        	  NOT NULL,
	url                 			CHARACTER VARYING NOT NULL,
	title          			        CHARACTER VARYING NOT NULL,
	content         			    CHARACTER VARYING NOT NULL,
	state             			    CHARACTER VARYING NOT NULL,
	create_date                 	TIMESTAMPTZ	      NOT NULL,
	edit_date 			            TIMESTAMPTZ		  NOT NULL,

	PRIMARY KEY (id),
	FOREIGN KEY (creator_id) REFERENCES Users (id)
);

# --- !Downs
DROP TABLE Articles;
DROP TABLE Users;
