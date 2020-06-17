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
/*
BEGIN;
ALTER TABLE game_versions DROP CONSTRAINT game_versions_game_fkey;
ALTER TABLE game_versions ADD CONSTRAINT game_versions_game_fkey FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE game_versions DROP CONSTRAINT game_versions_platform_fkey;
ALTER TABLE game_versions ADD CONSTRAINT game_versions_platform_fkey FOREIGN KEY(platform) REFERENCES platforms ON DELETE CASCADE;
COMMIT;
*/

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
	locale VARCHAR(25) NOT NULL DEFAULT 'en',
	PRIMARY KEY(user_id)
);
--ALTER TABLE users ADD COLUMN locale VARCHAR(25) NOT NULL DEFAULT 'en';

CREATE TABLE IF NOT EXISTS releases(
	game INT NOT NULL,
	region INT NOT NULL,
	release_date DATE,
	PRIMARY KEY(game, region),
	FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE,
	FOREIGN KEY(region) REFERENCES regions ON DELETE CASCADE
);
/*
BEGIN;
ALTER TABLE releases DROP CONSTRAINT releases_game_fkey;
ALTER TABLE releases ADD CONSTRAINT releases_game_fkey FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE releases DROP CONSTRAINT releases_region_fkey;
ALTER TABLE releases ADD CONSTRAINT releases_region_fkey FOREIGN KEY(region) REFERENCES platforms ON DELETE CASCADE;
COMMIT;
*/

CREATE TABLE IF NOT EXISTS scores(
	user_id INT NOT NULL,
	game INT NOT NULL,
	score INT NOT NULL,
	PRIMARY KEY(user_id, game),
	FOREIGN KEY(user_id) REFERENCES users ON DELETE CASCADE,
	FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE
);
/*
BEGIN;
ALTER TABLE scores DROP CONSTRAINT scores_game_fkey;
ALTER TABLE scores ADD CONSTRAINT scores_game_fkey FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE scores DROP CONSTRAINT scores_user_id_fkey;
ALTER TABLE scores ADD CONSTRAINT scores_user_id_fkey FOREIGN KEY(user_id) REFERENCES users ON DELETE CASCADE;
COMMIT;
*/


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
--	user INT NOT NULL,
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
	FOREIGN KEY(user_id) REFERENCES users ON DELETE CASCADE,
	FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE,
	FOREIGN KEY(playstyle) REFERENCES playstyles ON DELETE CASCADE,
	FOREIGN KEY(platform) REFERENCES platforms ON DELETE CASCADE
);
/*
BEGIN;
ALTER TABLE runs DROP CONSTRAINT runs_game_fkey;
ALTER TABLE runs ADD CONSTRAINT runs_game_fkey FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE runs DROP CONSTRAINT runs_platform_fkey;
ALTER TABLE runs ADD CONSTRAINT runs_platform_fkey FOREIGN KEY(platform) REFERENCES platforms ON DELETE CASCADE;
ALTER TABLE runs DROP CONSTRAINT runs_user_id_fkey;
ALTER TABLE runs ADD CONSTRAINT runs_user_id_fkey FOREIGN KEY(user_id) REFERENCES users ON DELETE CASCADE;
ALTER TABLE runs DROP CONSTRAINT runs_playstyle_fkey;
ALTER TABLE runs ADD CONSTRAINT runs_playstyle_fkey FOREIGN KEY(playstyle) REFERENCES playstyles ON DELETE CASCADE;
COMMIT;
*/

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
	FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE,
	FOREIGN KEY(genre) REFERENCES genres ON DELETE CASCADE
);
/*
BEGIN;
ALTER TABLE classifications DROP CONSTRAINT classifications_game_fkey;
ALTER TABLE classifications ADD CONSTRAINT classifications_game_fkey FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE classifications DROP CONSTRAINT classifications_genre_fkey;
ALTER TABLE classifications ADD CONSTRAINT classifications_genre_fkey FOREIGN KEY(genre) REFERENCES genres ON DELETE CASCADE;
COMMIT;
*/

CREATE TABLE IF NOT EXISTS backlogs(
	user_id INT NOT NULL,
	game INT NOT NULL,
	PRIMARY KEY(user_id, game),
	FOREIGN KEY(user_id) REFERENCES users ON DELETE CASCADE,
	FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE
);
/*
BEGIN;
ALTER TABLE backlogs DROP CONSTRAINT backlogs_game_fkey;
ALTER TABLE backlogs ADD CONSTRAINT backlogs_game_fkey FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE backlogs DROP CONSTRAINT backlogs_user_id_fkey;
ALTER TABLE backlogs ADD CONSTRAINT backlogs_user_id_fkey FOREIGN KEY(user_id) REFERENCES users ON DELETE CASCADE;
COMMIT;
*/

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
	FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE,
	FOREIGN KEY(developer) REFERENCES developers ON DELETE CASCADE
);
/*
BEGIN;
ALTER TABLE development DROP CONSTRAINT development_game_fkey;
ALTER TABLE development ADD CONSTRAINT development_game_fkey FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE development DROP CONSTRAINT development_developer_fkey;
ALTER TABLE development ADD CONSTRAINT development_developer_fkey FOREIGN KEY(developer) REFERENCES developers ON DELETE CASCADE;
COMMIT;
*/

CREATE TABLE IF NOT EXISTS publishing(
	game INT NOT NULL,
	publisher INT NOT NULL,
	PRIMARY KEY(game, publisher),
	FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE,
	FOREIGN KEY(publisher) REFERENCES publishers ON DELETE CASCADE
);
/*
BEGIN;
ALTER TABLE publishing DROP CONSTRAINT publishing_game_fkey;
ALTER TABLE publishing ADD CONSTRAINT publishing_game_fkey FOREIGN KEY(game) REFERENCES games ON DELETE CASCADE;
ALTER TABLE publishing DROP CONSTRAINT publishing_publisher_fkey;
ALTER TABLE publishing ADD CONSTRAINT publishing_publisher_fkey FOREIGN KEY(publisher) REFERENCES publishers ON DELETE CASCADE;
COMMIT;
*/

CREATE TABLE IF NOT EXISTS images(
    image      SERIAL,
    image_name VARCHAR(60) NOT NULL,
    image_data BYTEA,
    PRIMARY KEY(image)
);

DO '
BEGIN
   IF (SELECT count(*) FROM images) = 0 THEN
	INSERT INTO images(image_name, image_data) VALUES(''default-game-cover.png'', decode(''iVBORw0KGgoAAAANSUhEUgAAAPgAAAFACAYAAACRP7GnAAAACXBIWXMAAAsSAAALEgHS3X78AAALe0lEQVR4nO3d62scZR/H4YlGrVpFUFREQfGlL0T0P+8f4StBEHwhCILgCcVaj7W1ffitGZ9NsufdzD3zneuCkjSbbNPd/cx9z3HP7ty587gDIj3haYVcAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodgAodg555cxujRo0fdw4cP//vNzs/PuyeeMB7tS+AH+Ouvv7p79+51//zzz+KHz87OuqeeemrxeX2sv5dnnnlm8fHJJ59c/KFbPGb943b//v3Fx8ePH3cPHjxYfF4f6+9X1eP34osvdrdu3bp2G+sJfA/1wrx79273999/X/qhekH2X1u+7bfffrt2508//fTiY41GNSp1FwuFfnTqb5+a/v9dI28fa43A9fflgI957H/++efF41Oh9wtUNhP4DupFWrH+/vvvR9/X1YXDOsuj/vJCoX9hDzFlXZ4mV6BXY10ejYdSj9+PP/7YPf/8893t27dN27cQ+BZ//vln98svv6ycNt6k5Xi2LRT6BcDyQqFfPVhefej10+Dl0Xaff28MamH7xx9/dC+88MIidlYT+Br1Iq/17GOnlkNYFeSq1YM0tZCq56gWwjVtn+rqzU0S+BU1iv3666+LFw3TUAvhn376abEBrkK3QfP/BL6kwq6p39DTcU6j9m7Ulvmastcf6+cCX7i624vpqoVzrZ700/a571abdeDrdnsxrHoOvv766/+eh1qXfuutt7qXXnrpqOfWbrWZHqpaW49rxP7hhx/E3VjF/eWXX156Hurz+lrddqx+t1o93/W8z83sRvBT7fb65JNPrn2t9+GHH177GqvVyL1O3XbMKL5srrvVZhP4lHZ7zcmmGdSm2w4xx91q8YHb7cVVc9qtFh243V7tbduAVn9fN1Ivj7A3sSGu362WPG2P3shWu0vE3c4uG9Aq0nX6225yQ1y9PpJ3j0YH7tDFtrZtQCs1Ar/77ruXnqv6vL7Wj8673M8xkl8n0VP0TdO/XW3aWr7Oup+Z29b1TY/98m0V8qap9k1viEsOPHoE78+ognWWz8VPZIpOk8d/0203dT+rpL9G4neTnWKazmFqI1ltCFtleePati3ku97PIdIDjz9U1Sjezi4b0HbZQr7L/RzKCD5xtR4+h4sfjNW2DWibtoIvH6q67X4Okb7+3RnBaW3T6tOm207BoaohjlkPX7dra92usE0/w7jMIfBZnC5qFB+vTc/NpttOQeAh7A8fr01bwTfddqw5rH93RnBau8kt5JvM5TUxm/PB7Q8fr5vYQr6NwMOMOfA6m6nOT65LC109b/3ZZ5/tXnnlle7ll18e9XnLdbWU5bOy6nd97rnnrn3fWMwl8LM7d+7M4nzKirsiGps6mOOrr77aespiBfP2228PPtJt88033ywe11ULz/qdX3311e61114b1cKp1r9roTkHs7no4hiX2BVGHbG1y/nI9T31vWNZSNWI/fnnn3fffvvtyri7i9+5bv/ss88WF98Yizltk5nVVVXH9MT2I/e+6mdOcbXRY1TcX3zxxbXViXUq9Pr+sSycBB5qLE9sveAPibu3y5T+pvQziUP+/eUTSloSeKix7A+vkeyYQPuNci18//33B0d67ILtFOay/7tnBG+gtpYf6xT3cYh6s4hj1Lp4y1F8bsdEzO6dTcbwBJ/iEs4tLgNdcZ5i1aDlNgSBh3NU2+FOtSW8NtK1IvBwjktvr9UUfW7r350RnDmZ43M/y3cXbf1E1+GnY7iPfZ1q9nP79u1rXxuCwGei9RN9isMkWxxqWW/xcwqtjlEX+Ey0Xg8/9sSR+tm6j1OrADb9qZG33qzvGPW7tziefo7r390c3x+8u3ghn52dNXvfsv7EkXWXAt7mgw8+6F5//fUt33Uz3nvvve7jjz8++L7feOONa18bQotVmjGY5QheWr+bZI1iFfm+3n///WZxdxezj3feeefa13dRU/w6u2xotTAX+MyM4e1iK5a6csku0/Xz8/Puo48+utHLGO2qRvE333xzr5+puOv/2kI913OcnndzOh98lTqiqsURYavUe1XXyRj37t27dGut81bUFVStR47Jd99913366afdw4cP1/5W/Tnhrabmpf795Df532TWgddhl8ceW30qdVGEKY4yDx48WIReJ6HU58tqa3nrK9HU1HxsF8kY0iw3svXqhTeWSznV73Dr1q1rXx+7mlXUDOPqqsO6q7wMbQyrYi3Ndh28d6p9u8e6OvpN3VjO+x7bas3QZh94vQjGsH6WdMXXsSys5j56dwL/1xhG8aQRfAwLq1poT3GV59QEfrEhpvaVtlQH3bS6DNOpjWFhNZZVr9YEfmEM07mUafqm3WZDqIW104L/JfALFXjrUTxlmt76/zHnA1uu8ihcqBdE63W2hMDtGhsXgS9pvd6WMEW/f//+ta8NqbanGL3/zyOxZAxbXqc+irde/7Zx7TKBX9F6ejf1UbzlAmosxzSMicCvaH3005RH8EePHjXd1Wf0vk7gK7QcxVtPcY/RcvbRn1fAZQJfoTbUtJrqTXkEb/m7G71XE/gaLa8AMtX18Fa/95yv2LKNwNdoeeBL611Nh2o1gtvvvZ7A16h9qa0u7zvF9fDauNbiIpa1EBb4egLfoNULZ4rr4a2m53XcggNb1vPIbFAb2lqs29VoWLucpqTVQsnGtc0EvkWrafrUNrS1CLxGbwe2bCbwLfp3+hja1KbpLRZI1r23E/gOWoziUxrBWyyM6mhDB7ZsJ/AdtDjwZUojuNF7vAS+o6FfUFO6hNPQC6NWGz+nSOA7anHdtqlM04feby/u3Ql8R7WvdehRfCrT9CF/Twe27Efgexh6Y9sUAh96llHPgQNbdueR2sPQ635TmKIPfdy80Xs/At+TafplQ65/tzyNd6oEvqeh97+OfRQfcgHU6qjCKRP4AYYcxcc8gg95iaZWRxROncAPMOQx0GM+dXTI2YXR+zCzfn/wY9RZTHfv3r3xf2fMI3gdjDPEqFpbze37PozAD1QvuLm/6DwG42eKDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDsEEDqm6rvsffv4bMIP7NrAAAAAASUVORK5CYII='', ''base64'')) ON CONFLICT DO NOTHING;
   END IF;
END';

CREATE TABLE IF NOT EXISTS roles(
	role 		SERIAL,
	role_name	VARCHAR(50),
	PRIMARY KEY(role)
);
DO '
BEGIN 
   IF (SELECT count(*) FROM roles) = 0 THEN
	INSERT INTO roles(role_name) VALUES(''Admin'') ON CONFLICT DO NOTHING;
   END IF;
END';

CREATE TABLE IF NOT EXISTS role_assignments(
	user_id INT NOT NULL,
	role INT NOT NULL,
	PRIMARY KEY(user_id, role),
	FOREIGN KEY(user_id) REFERENCES users ON DELETE CASCADE,
	FOREIGN KEY(role) REFERENCES roles ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tokens(
	user_id INT NOT NULL,
	token VARCHAR(250),
	expiration DATE,
	PRIMARY KEY(user_id),
	FOREIGN KEY(user_id) REFERENCES users ON DELETE CASCADE
);