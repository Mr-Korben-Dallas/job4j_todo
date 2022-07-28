CREATE TABLE IF NOT EXISTS items (
   id bigserial PRIMARY KEY,
   name TEXT NOT NULL,
   description TEXT NOT NULL,
   is_done boolean NOT NULL DEFAULT false,
   created TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
   id bigserial PRIMARY KEY,
   name TEXT NOT NULL,
   login varchar NOT NULL,
   password TEXT NOT NULL
);

ALTER TABLE accounts ADD CONSTRAINT login_unique UNIQUE (login);