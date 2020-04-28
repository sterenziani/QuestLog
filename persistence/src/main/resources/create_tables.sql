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
	FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE,
	FOREIGN KEY(platform) REFERENCES platforms ON DELETE CASCADE
);
/**ALTER TABLE game_versions ADD FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE game_versions ADD FOREIGN KEY(platform) REFERENCES platforms ON DELETE CASCADE;
**/
CREATE TABLE IF NOT EXISTS regions(
	region SERIAL,
	region_name VARCHAR(15) NOT NULL UNIQUE,
	region_short VARCHAR(5) NOT NULL UNIQUE,
	PRIMARY KEY(region)
);

CREATE TABLE IF NOT EXISTS users(
	user_id SERIAL,
	username VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	PRIMARY KEY(user_id)
);
/**
ALTER TABLE users ADD COLUMN IF NOT EXISTS email VARCHAR(100) UNIQUE NOT NULL;
**/
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

DO '
BEGIN 
   IF (SELECT count(*) FROM playstyles) < 4 THEN 
      INSERT INTO playstyles(playstyle_name) VALUES(''MainStory'') ON CONFLICT DO NOTHING;
      INSERT INTO playstyles(playstyle_name) VALUES(''MainStoryExtras'') ON CONFLICT DO NOTHING;
      INSERT INTO playstyles(playstyle_name) VALUES(''Completionist'') ON CONFLICT DO NOTHING;
      INSERT INTO playstyles(playstyle_name) VALUES(''Speedrun'') ON CONFLICT DO NOTHING;
   END IF;
END';

--CREATE TABLE IF NOT EXISTS reviews(
--	review SERIAL,
--	user_id INT NOT NULL,
--	game INT NOT NULL,
--	platform INT NOT NULL,
--	score INT NOT NULL,
--	body TEXT,
--	post_date TIMESTAMP,
--	PRIMARY KEY(review),
--	FOREIGN KEY(user_id) REFERENCES users,
--	FOREIGN KEY(game) REFERENCES games,
--	FOREIGN KEY(platform) REFERENCES platforms
--);

CREATE TABLE IF NOT EXISTS runs(
	run SERIAL,
	user_id INT NOT NULL,
	game INT NOT NULL,
	platform INT NOT NULL,
	playstyle INT NOT NULL,
	time INT NOT NULL,
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

DO '
BEGIN 
   IF (SELECT count(*) FROM genres) < 21 THEN
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Action'', ''https://media.rawg.io/media/games/b7b/b7b8381707152afc7d91f5d95de70e39.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Indie'', ''https://media.rawg.io/media/games/0be/0bea0a08a4d954337305391b778a7f37.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Adventure'', ''https://media.rawg.io/media/games/4cb/4cb855e8ef1578415a928e53c9f51867.png'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''RPG'', ''https://media.rawg.io/media/games/d0f/d0f91fe1d92332147e5db74e207cfc7a.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Shooter'', ''https://media.rawg.io/media/games/73e/73eecb8909e0c39fb246f457b5d6cbbe.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Strategy'', ''https://media.rawg.io/media/games/f95/f95ec06eddda5c5bf206618c49cd3e68.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Casual'', ''https://media.rawg.io/media/screenshots/dc2/dc2814dc50d61be1ea4fcd5d3c03ddb6.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Simulation'', ''https://media.rawg.io/media/games/b4e/b4e4c73d5aa4ec66bbf75375c4847a2b.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Arcade'', ''https://media.rawg.io/media/screenshots/6fe/6fe228662a253cd929cc78a103541ee0.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Puzzle'', ''https://media.rawg.io/media/screenshots/42d/42d770eb49f2ba01cd4045e0d92af7a9.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Platformer'', ''https://media.rawg.io/media/games/4cf/4cfc6b7f1850590a4634b08bfab308ab.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Racing'', ''https://media.rawg.io/media/games/9e5/9e52a797f049e701d4eee84774a99007.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Sports'', ''https://media.rawg.io/media/games/cc5/cc576aa274780702ef93463f5410031e.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''MMO'', ''https://media.rawg.io/media/screenshots/266/26605ea274a4dcada95f3f02b30fc2ed.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Family'', ''https://media.rawg.io/media/screenshots/904/904809185a905c430f6c36f0ebf5353c.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Fighting'', ''https://media.rawg.io/media/screenshots/3e2/3e22f928180309b9f624628e71fdc447.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''BoardGames'', ''https://media.rawg.io/media/screenshots/c25/c252e2023355e98787402c4bdd90f775.jpeg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Educational'', ''https://media.rawg.io/media/screenshots/b10/b1037cefbbf94070dd4fae7ffbbd6999.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Card'', ''https://media.rawg.io/media/screenshots/3e7/3e728c858ebd673e099cab3558a47d42.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Horror'', ''https://media.rawg.io/media/screenshots/3e7/3e728c858ebd673e099cab3558a47d42.jpg'') ON CONFLICT DO NOTHING;
	INSERT INTO genres(genre_name, genre_logo) VALUES(''Stealth'', ''https://media.rawg.io/media/screenshots/3e7/3e728c858ebd673e099cab3558a47d42.jpg'') ON CONFLICT DO NOTHING;
   END IF;
END';

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