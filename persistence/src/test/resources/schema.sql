CREATE TABLE IF NOT EXISTS users(
	id INTEGER IDENTITY PRIMARY KEY,
    username varchar(100)
);

CREATE TABLE IF NOT EXISTS platforms(
	platform INTEGER IDENTITY,
	platform_name VARCHAR(30) UNIQUE,
	platform_name_short VARCHAR(6) UNIQUE,
	platform_logo varchar(300) UNIQUE
);

CREATE TABLE IF NOT EXISTS games(
	game INTEGER IDENTITY,
	title VARCHAR(100) UNIQUE,
	cover varchar(300),
	description varchar(3000)
);

CREATE TABLE IF NOT EXISTS game_versions(
	game INT,
	platform INT
);