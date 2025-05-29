CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    baggage_weight DOUBLE PRECISION,
    travel_class VARCHAR(50)
);

CREATE TABLE flights (
    id SERIAL PRIMARY KEY,
    destination VARCHAR(255),
    origin VARCHAR(255),
    departure_time TIMESTAMP,
    baggage_limit DOUBLE PRECISION,
    available_seats INTEGER,
    started BOOLEAN DEFAULT FALSE
);

CREATE TABLE flight_users (
    flight_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (flight_id, user_id),
    FOREIGN KEY (flight_id) REFERENCES flights(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);