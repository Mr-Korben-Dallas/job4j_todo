CREATE TABLE IF NOT EXISTS categories (
   id bigserial PRIMARY KEY,
   name TEXT NOT NULL
);

INSERT INTO categories(name) VALUES ('just');
INSERT INTO categories(name) VALUES ('church');
INSERT INTO categories(name) VALUES ('owe');
