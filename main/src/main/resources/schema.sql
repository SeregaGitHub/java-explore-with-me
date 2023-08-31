DROP TABLE IF EXISTS event_compilation;
DROP TABLE IF EXISTS participation_request;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS compilation;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
  user_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  user_name varchar(250) NOT NULL,
  user_email varchar(254) NOT NULL,
  CONSTRAINT pk_users_user_id PRIMARY KEY (user_id),
  CONSTRAINT uk_users_user_email UNIQUE (user_email)
);

CREATE TABLE IF NOT EXISTS category (
  category_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  category_name varchar(255) NOT NULL,
  CONSTRAINT pk_category_category_id PRIMARY KEY (category_id),
  CONSTRAINT uk_category_category_name UNIQUE (category_name)
);

CREATE TABLE IF NOT EXISTS location (
  location_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  lat float NOT NULL,
  lon float NOT NULL,
  CONSTRAINT pk_location_location_id PRIMARY KEY (location_id)
);

CREATE TABLE IF NOT EXISTS compilation (
  compilation_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  compilation_pinned boolean NOT NULL,
  compilation_title varchar(50) NOT NULL,
  CONSTRAINT pk_compilation_compilation_id PRIMARY KEY (compilation_id)
);

CREATE TABLE IF NOT EXISTS event (
  event_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation varchar(2000) NOT NULL,
  category_id INTEGER NOT NULL,
  created_on timestamp NOT NULL,
  description varchar(7000) NOT NULL,
  event_date timestamp NOT NULL,
  initiator_id INTEGER NOT NULL,
  location_id INTEGER NOT NULL,
  paid boolean NOT NULL,
  participant_limit INTEGER NOT NULL,
  published_on timestamp,
  request_moderation boolean NOT NULL,
  state varchar(32) NOT NULL,
  event_title varchar(120) NOT NULL,
  CONSTRAINT pk_event_event_id PRIMARY KEY (event_id),
  CONSTRAINT fk_event_location_id FOREIGN KEY (location_id) REFERENCES location (location_id),
  CONSTRAINT fk_event_initiator_id FOREIGN KEY (initiator_id) REFERENCES users (user_id),
  CONSTRAINT fk_event_category_id FOREIGN KEY (category_id) REFERENCES category (category_id)
);

CREATE TABLE IF NOT EXISTS participation_request (
  participation_request_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created timestamp NOT NULL,
  event_id INTEGER NOT NULL,
  requester_id INTEGER NOT NULL,
  status varchar(32) NOT NULL,
  CONSTRAINT pk_participation_request_participation_request_id PRIMARY KEY (participation_request_id),
  CONSTRAINT fk_participation_request_event_id FOREIGN KEY (event_id) REFERENCES event (event_id),
  CONSTRAINT fk_participation_request_requester_id FOREIGN KEY (requester_id) REFERENCES users (user_id),
  CONSTRAINT uk_category_event_id_requester_id UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS event_compilation (
  compilation_id  INTEGER NOT NULL,
  event_id INTEGER NOT NULL,
  CONSTRAINT pk_event_compilation_compilation_id_event_id PRIMARY KEY (compilation_id, event_id),
  CONSTRAINT fk_event_compilation_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilation (compilation_id),
  CONSTRAINT fk_event_compilation_event_id FOREIGN KEY (event_id) REFERENCES event (event_id)
);