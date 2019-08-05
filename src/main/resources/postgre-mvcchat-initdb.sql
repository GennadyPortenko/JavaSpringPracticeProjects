-- DROP SCHEMA IF EXISTS public CASCADE;
-- CREATE SCHEMA public;

DROP TABLE IF EXISTS account CASCADE;
CREATE TABLE account (
  user_id SERIAL PRIMARY KEY
, name VARCHAR(50) NOT NULL
, email VARCHAR(150) NOT NULL
, password VARCHAR(200) NOT NULL
, active INTEGER NOT NULL
);

DROP TABLE IF EXISTS message CASCADE;
CREATE TABLE message (
  message_id BIGSERIAL PRIMARY KEY
, datetime TIMESTAMP WITH TIME ZONE NOT NULL
, text TEXT NOT NULL
, deleted TIMESTAMP WITH TIME ZONE
, user_fk INTEGER REFERENCES account(user_id) -- ON UPDATE CASCADE ON DELETE CASCADE
);

DROP TABLE IF EXISTS message_message_to_reply CASCADE;
CREATE TABLE message_message_to_reply (
  message_id BIGINT REFERENCES message(message_id) -- ON UPDATE CASCADE ON DELETE CASCADE
, message_to_reply_id BIGINT REFERENCES message(message_id) -- ON UPDATE CASCADE ON DELETE CASCADE
, CONSTRAINT message_message_to_reply_pkey PRIMARY KEY (message_id, message_to_reply_id)
);

DROP TABLE IF EXISTS role CASCADE;
CREATE TABLE role (
  role_id SERIAL PRIMARY KEY
, role VARCHAR(20) NOT NULL
);

DROP TABLE IF EXISTS user_role CASCADE;
CREATE TABLE user_role (
  user_id INTEGER REFERENCES account(user_id)
, role_id INTEGER REFERENCES role(role_id)
, CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id)
);

-- roles initialization

INSERT INTO role (role_id, role)
VALUES (1, 'ADMIN'), (2, 'USER')
ON CONFLICT (role_id) DO UPDATE
  SET role = excluded.role;
