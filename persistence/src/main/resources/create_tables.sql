--drop schema public cascade;
--create schema public;

CREATE TABLE IF NOT EXISTS developers(
	developer SERIAL,
	developer_name VARCHAR(75) NOT NULL UNIQUE,
	developer_logo TEXT,
	PRIMARY KEY(developer)
);

CREATE TABLE IF NOT EXISTS publishers(
	publisher SERIAL,
	publisher_name VARCHAR(75) NOT NULL UNIQUE,
	publisher_logo TEXT,
	PRIMARY KEY(publisher)
);

CREATE TABLE IF NOT EXISTS games(
	game SERIAL,
	title VARCHAR(100) NOT NULL UNIQUE,
	cover TEXT,
	description TEXT,
	PRIMARY KEY(game)
);

CREATE TABLE IF NOT EXISTS platforms(
	platform SERIAL,
	platform_name VARCHAR(30) NOT NULL UNIQUE,
	platform_name_short VARCHAR(6) NOT NULL UNIQUE,
	platform_logo TEXT,
	PRIMARY KEY(platform)
);

CREATE TABLE IF NOT EXISTS game_versions(
	game INT NOT NULL,
	platform INT NOT NULL,
	PRIMARY KEY(game, platform),
	FOREIGN KEY(game) REFERENCES games,
	FOREIGN KEY(platform) REFERENCES platforms
);

CREATE TABLE IF NOT EXISTS regions(
	region SERIAL,
	region_name VARCHAR(15) NOT NULL UNIQUE,
	region_short VARCHAR(5) NOT NULL UNIQUE,
	PRIMARY KEY(region)
);

--CREATE TABLE IF NOT EXISTS users(
--	id SERIAL,
--	username VARCHAR(100),
--	password VARCHAR(255),
--	PRIMARY KEY (id)
--);

CREATE TABLE IF NOT EXISTS users(
	user_id SERIAL,
	username VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
--	email VARCHAR(100) UNIQUE NOT NULL,
	PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS releases(
	game INT NOT NULL,
	region INT NOT NULL,
	release_date DATE,
	PRIMARY KEY(game, region),
	FOREIGN KEY(game) REFERENCES games,
	FOREIGN KEY(region) REFERENCES regions
);

CREATE TABLE IF NOT EXISTS scores(
	user_id INT NOT NULL,
	game INT NOT NULL,
	score INT NOT NULL,
	PRIMARY KEY(user_id, game),
	FOREIGN KEY(user_id) REFERENCES users,
	FOREIGN KEY(game) REFERENCES games
);

CREATE TABLE IF NOT EXISTS playstyles(
	playstyle SERIAL,
	playstyle_name VARCHAR(25) NOT NULL UNIQUE,
	PRIMARY KEY(playstyle)
);

CREATE TABLE IF NOT EXISTS reviews(
	review SERIAL,
	user_id INT NOT NULL,
	game INT NOT NULL,
	platform INT NOT NULL,
	score INT NOT NULL,
	body TEXT,
	post_date TIMESTAMP,
	PRIMARY KEY(review),
	FOREIGN KEY(user_id) REFERENCES users,
	FOREIGN KEY(game) REFERENCES games,
	FOREIGN KEY(platform) REFERENCES platforms
);

CREATE TABLE IF NOT EXISTS runs(
	run SERIAL,
	user_id INT NOT NULL,
	game INT NOT NULL,
	platform INT NOT NULL,
	playstyle INT NOT NULL,
	time INTERVAL NOT NULL,
	PRIMARY KEY(run),
	FOREIGN KEY(user_id) REFERENCES users,
	FOREIGN KEY(game) REFERENCES games,
	FOREIGN KEY(playstyle) REFERENCES playstyles,
	FOREIGN KEY(platform) REFERENCES platforms
);

CREATE TABLE IF NOT EXISTS genres(
	genre SERIAL,
	genre_name VARCHAR(15) NOT NULL UNIQUE,
	genre_logo TEXT,
	PRIMARY KEY(genre)
);

CREATE TABLE IF NOT EXISTS classifications(
	game INT NOT NULL,
	genre INT NOT NULL,
	PRIMARY KEY(game, genre),
	FOREIGN KEY(game) REFERENCES games,
	FOREIGN KEY(genre) REFERENCES genres
);

CREATE TABLE IF NOT EXISTS backlogs(
	user_id INT NOT NULL,
	game INT NOT NULL,
	PRIMARY KEY(user_id, game),
	FOREIGN KEY(user_id) REFERENCES users,
	FOREIGN KEY(game) REFERENCES games
);

--CREATE TABLE IF NOT EXISTS lists(
--	list SERIAL,
--	user_id INT NOT NULL,
--	list_name VARCHAR(25) NOT NULL,
--	PRIMARY KEY(list),
--	FOREIGN KEY(user_id) REFERENCES users
--);

--CREATE TABLE IF NOT EXISTS list_contents(
--	game INT NOT NULL,
--	list INT NOT NULL,
--	date_added DATE NOT NULL,
--	PRIMARY KEY(game, list)
--);

CREATE TABLE IF NOT EXISTS development(
	game INT NOT NULL,
	developer INT NOT NULL,
	PRIMARY KEY(game, developer),
	FOREIGN KEY(game) REFERENCES games,
	FOREIGN KEY(developer) REFERENCES developers
);

CREATE TABLE IF NOT EXISTS publishing(
	game INT NOT NULL,
	publisher INT NOT NULL,
	PRIMARY KEY(game, publisher),
	FOREIGN KEY(game) REFERENCES games,
	FOREIGN KEY(publisher) REFERENCES publishers
);