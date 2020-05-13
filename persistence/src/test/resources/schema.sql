CREATE TABLE IF NOT EXISTS users(
	user_id INTEGER IDENTITY,
    username VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    email VARCHAR(100) UNIQUE,   
    locale VARCHAR(25) DEFAULT 'en',
);

CREATE TABLE IF NOT EXISTS platforms(
	platform INTEGER IDENTITY,
	platform_name VARCHAR(30) UNIQUE,
	platform_name_short VARCHAR(6) UNIQUE,
	platform_logo VARCHAR(300)
);

CREATE TABLE IF NOT EXISTS games(
	game INTEGER IDENTITY,
	title VARCHAR(100) UNIQUE,
	cover VARCHAR(300),
	description VARCHAR(3000)
);

CREATE TABLE IF NOT EXISTS game_versions(
	game INT,
	platform INT
);

CREATE TABLE IF NOT EXISTS developers(
	developer INTEGER IDENTITY,
	developer_name VARCHAR(75) UNIQUE,
	developer_logo VARCHAR(300)
);

CREATE TABLE IF NOT EXISTS publishers(
	publisher INTEGER IDENTITY,
	publisher_name VARCHAR(75) UNIQUE,
	publisher_logo VARCHAR(300)
);

CREATE TABLE IF NOT EXISTS development(
	game INT,
	developer INT
);

CREATE TABLE IF NOT EXISTS publishing(
	game INT,
	publisher INT
);

CREATE TABLE IF NOT EXISTS genres(
	genre INTEGER IDENTITY,
	genre_name VARCHAR(15) UNIQUE,
	genre_logo VARCHAR(300)
);

CREATE TABLE IF NOT EXISTS classifications(
	game INT,
	genre INT
);

CREATE TABLE IF NOT EXISTS regions(
	region INTEGER IDENTITY,
	region_name VARCHAR(15) UNIQUE,
	region_short VARCHAR(5) UNIQUE
);

CREATE TABLE IF NOT EXISTS releases(
	game INT,
	region INT,
	release_date DATE
);

CREATE TABLE IF NOT EXISTS scores(
	user_id INT,
	game INT,
	score INT,
);

CREATE TABLE IF NOT EXISTS reviews(
	review INTEGER IDENTITY,
	user_id INT,
	game INT,
	platform INT,
	score INT,
	body VARCHAR(300),
	post_date TIMESTAMP,

);

CREATE TABLE IF NOT EXISTS runs(
	run INTEGER IDENTITY,
	user_id INT,
	game INT,
	platform INT,
	playstyle INT,
	time INT,
);

CREATE TABLE IF NOT EXISTS playstyles(
	playstyle INTEGER IDENTITY,
	playstyle_name VARCHAR(25),
);

CREATE TABLE IF NOT EXISTS images(
    image      INTEGER IDENTITY,
    image_name VARCHAR(60) NOT NULL,
    image_data BINARY
);

CREATE TABLE IF NOT EXISTS roles(
	role 		INTEGER IDENTITY,
	role_name	VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS role_assignments(
	user_id INT NOT NULL,
	role INT NOT NULL
);

CREATE TABLE IF NOT EXISTS backlogs(
	user_id INT NOT NULL,
	game INT NOT NULL,
);