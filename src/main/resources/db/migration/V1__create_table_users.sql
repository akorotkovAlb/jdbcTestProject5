CREATE TABLE users(
	id bigint PRIMARY KEY,
	"name" varchar(255) NOT NULL,
	birthday date NOT NULL
);

CREATE SEQUENCE users_seq START 1;

ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_seq');