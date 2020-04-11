--drop schema public cascade;
--create schema public;


CREATE TABLE IF NOT EXISTS users(
	id SERIAL PRIMARY KEY,
    username varchar(100)
);