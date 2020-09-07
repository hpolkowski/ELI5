# ELI5 Language changing

# --- !Ups
ALTER TABLE Articles ADD COLUMN lang CHARACTER VARYING NOT NULL DEFAULT 'us';
ALTER TABLE Newsletter ADD COLUMN lang CHARACTER VARYING NOT NULL DEFAULT 'us';
ALTER TABLE Users ADD COLUMN lang CHARACTER VARYING NOT NULL DEFAULT 'us';

# --- !Downs
ALTER TABLE Users DROP COLUMN lang;
ALTER TABLE Newsletter DROP COLUMN lang;
ALTER TABLE Articles DROP COLUMN lang;