-- Consoles
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(10, 'Wii U', 'WII U', 'http://img4.wikia.nocookie.net/__cb20121024163701/mario/images/d/dc/Wii_U_(Logo).png') ON CONFLICT DO NOTHING;
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(4, 'PC', 'PC', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Windows_logo_-_2012.svg/1024px-Windows_logo_-_2012.svg.png') ON CONFLICT DO NOTHING;
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(19, 'PlayStation Vita', 'PSVITA', 'http://www.jp.square-enix.com/dc_vita/en/images/main_logo_vita.png') ON CONFLICT DO NOTHING;
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(18, 'PlayStation 4', 'PS4', 'https://i.ytimg.com/vi/56NxUWEFpL0/maxresdefault.jpg') ON CONFLICT DO NOTHING;
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(1, 'Xbox One', 'XBONE', 'http://4.bp.blogspot.com/-UcjPVSovGlk/UZvWPYBtgGI/AAAAAAAAVmc/8dJQeuz796I/s1600/xbox+one+logo.jpg') ON CONFLICT DO NOTHING;
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(14, 'Xbox 360', 'X360', 'https://upload.wikimedia.org/wikipedia/it/thumb/c/c8/Xbox_360_-_Logo.svg/1200px-Xbox_360_-_Logo.svg.png') ON CONFLICT DO NOTHING;
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(16, 'PlayStation 3', 'PS3', 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/PlayStation_3_Logo.svg/1280px-PlayStation_3_Logo.svg.png') ON CONFLICT DO NOTHING;
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(7, 'Nintendo Switch', 'NS', 'https://upload.wikimedia.org/wikipedia/commons/0/04/Nintendo_Switch_logo%2C_square.png') ON CONFLICT DO NOTHING;
INSERT INTO platforms(platform, platform_name, platform_name_short, platform_logo) VALUES(8, 'Nintendo 3DS', '3DS', 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Nintendo_3ds_logo.svg/2171px-Nintendo_3ds_logo.svg.png') ON CONFLICT DO NOTHING;
INSERT INTO regions(region, region_name, region_short) VALUES(1, 'Worldwide', 'WW') ON CONFLICT DO NOTHING;

-- Genres
INSERT INTO genres(genre, genre_name, genre_logo) VALUES(4, 'Action','https://images.vexels.com/media/users/3/155801/isolated/preview/95382a27c1158603885f65feae07760e-simple-gun-icon-by-vexels.png')  ON CONFLICT DO NOTHING;
INSERT INTO genres(genre, genre_name, genre_logo) VALUES(83, 'Platformer','http://pngimg.com/uploads/joystick/joystick_PNG83314.png') ON CONFLICT DO NOTHING;
INSERT INTO genres(genre, genre_name, genre_logo) VALUES(51, 'Indie', 'http://icons.iconarchive.com/icons/iconsmind/outline/256/Tree-22-icon.png') ON CONFLICT DO NOTHING;
INSERT INTO genres(genre, genre_name, genre_logo) VALUES(3, 'Adventure', 'https://freepngimg.com/download/dragon/6-2-dragon-png-3.png') ON CONFLICT DO NOTHING;
INSERT INTO genres(genre, genre_name, genre_logo) VALUES(5, 'RPG', 'http://icons.iconarchive.com/icons/icons8/windows-8/256/Military-Sword-icon.png') ON CONFLICT DO NOTHING;

-- Devs and Publishers
INSERT INTO developers(developer, developer_name, developer_logo) VALUES(16257, 'Nintendo', 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/Nintendo_gray_logo.svg/1920px-Nintendo_gray_logo.svg.png') ON CONFLICT DO NOTHING;
INSERT INTO developers(developer, developer_name, developer_logo) VALUES(405, 'Ubisoft', 'https://upload.wikimedia.org/wikipedia/de/thumb/b/b3/Ubisoft-logo.svg/1024px-Ubisoft-logo.svg.png') ON CONFLICT DO NOTHING;
INSERT INTO developers(developer, developer_name, developer_logo) VALUES(4115, 'Ubisoft Montpellier', 'https://upload.wikimedia.org/wikipedia/en/thumb/5/51/Ubisoft_Montpellier_logotype_%282017%29.png/220px-Ubisoft_Montpellier_logotype_%282017%29.png') ON CONFLICT DO NOTHING;
INSERT INTO developers(developer, developer_name, developer_logo) VALUES(13799, 'StudioMDHR Entertainment Inc.', 'http://www.studiomdhr.com/press/images/logo.png') ON CONFLICT DO NOTHING;
INSERT INTO developers(developer, developer_name, developer_logo) VALUES(121012, 'StudioMDHR', 'http://www.studiomdhr.com/press/images/logo.png') ON CONFLICT DO NOTHING;

INSERT INTO publishers(publisher, publisher_name, publisher_logo) VALUES(10681, 'Nintendo', 'https://upload.wikimedia.org/wikipedia/commons/0/0d/Nintendo.svg') ON CONFLICT DO NOTHING;
INSERT INTO publishers(publisher, publisher_name, publisher_logo) VALUES(918, 'Ubisoft Entertainment', 'https://upload.wikimedia.org/wikipedia/de/thumb/b/b3/Ubisoft-logo.svg/1024px-Ubisoft-logo.svg.png') ON CONFLICT DO NOTHING;
INSERT INTO publishers(publisher, publisher_name, publisher_logo) VALUES(11164, 'StudioMDHR Entertainment Inc.', 'http://www.studiomdhr.com/press/images/logo.png') ON CONFLICT DO NOTHING;
INSERT INTO publishers(publisher, publisher_name, publisher_logo) VALUES(33005, 'StudioMDHR', 'http://www.studiomdhr.com/press/images/logo.png') ON CONFLICT DO NOTHING;

-- Super Mario 3D World - Exclusivo
INSERT INTO games(game, title, cover, description) VALUES(27974, 'Super Mario 3D World',
																   'https://media.rawg.io/media/games/0ef/0ef656cb91b6dc2382434000babc2725.jpg',
																   '<p>Work together with your friends or compete for the crown in the first<br />multiplayer 3D Mario game for the Wii U console. In the Super Mario™<br />3D World game, players can choose to play as Mario, Luigi, Princess<br />Peach, or Toad.</p>\n<p>Features:</p><p>Each character has unique abilities and play styles. For example,<br />Princess Peach glides over gaps, while Luigi has his trademark<br />extra-high jump.</p><p>The new cat transformation allows players to run on all fours, pounce<br />on enemies, attack in midair, or use their claws to scamper up walls<br />and goal poles.</p><p>Explore colorful 3D environments filled with obstacles and<br />contraptions like transparent pipes and vertical conveyor belts, which<br />add new puzzle elements.</p><p>Use the Wii U GamePad Controller&#39;s touch screen to search for hidden<br />blocks and freeze enemies in place.</p><p>Price effective March 11, 2016.</p>') ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(27974, 10) ON CONFLICT DO NOTHING;
INSERT INTO releases(game, region, release_date) VALUES(27974, 1, '2013-11-21') ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(27974, 4) ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(27974, 83) ON CONFLICT DO NOTHING;
INSERT INTO development(game, developer) VALUES(27974, 16257) ON CONFLICT DO NOTHING;
INSERT INTO publishing(game, publisher) VALUES(27974, 10681) ON CONFLICT DO NOTHING;

--Rayman Legends - Multiplatform - Multidev
INSERT INTO games(game, title, cover, description) VALUES(3771, 'Rayman Legends',
															'https://8keys.de/wp-content/uploads/2019/07/raymanlegendscv2overoriginal.jpg',
															'Rayman Legends is a fifth main title in the original series of platform games. The new Rayman chapter is a direct sequel to the 2011 game Rayman Origins. The game is set in an imaginary world inhabited by both friendly and hostile creatures. The main character Rayman, a shapeless magical being, accompanied by his closest friends is expected to protect the fictional world from evil. The story begins a century after the Origins events. Bubble Dreamers’ nightmares as well as the Magician (both are main antagonists) has grown up in their power, so only awakened after the 100-year sleep Rayman and his friends Globox and Teensies are able to stop the terrible villains. As the story takes place in various locations depending on the game stage, the environment, as well as difficulty, differs from time to time. The gameplay is about completing multiple platform levels, collecting lums and defeating enemies. In addition to Rayman, Globox and Teensies players can assume the roles of several new game characters. Rayman Legends also features a co-op game mode.') ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(3771, 10) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(3771, 4) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(3771, 19) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(3771, 18) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(3771, 1) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(3771, 14) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(3771, 16) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(3771, 7) ON CONFLICT DO NOTHING;
INSERT INTO releases(game, region, release_date) VALUES(3771, 1, '2013-08-29') ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(3771, 83) ON CONFLICT DO NOTHING;
INSERT INTO development(game, developer) VALUES(3771, 405) ON CONFLICT DO NOTHING;
INSERT INTO development(game, developer) VALUES(3771, 4115) ON CONFLICT DO NOTHING;
INSERT INTO publishing(game, publisher) VALUES(3771, 918) ON CONFLICT DO NOTHING;
-- Cuphead - Multiplatform - Multidev - Multipublisher
INSERT INTO games(game, title, cover, description) VALUES(28154, 'Cuphead',
														  'https://www.mobygames.com/images/covers/l/429819-cuphead-windows-apps-front-cover.jpg',
														  '<p>Hand-drawn 2D platformer in the style of 30s cartoons. 2D Dark Souls as the fans refer to the difficulty of this one. It took developers 6 years to create and polish their magnum opus. Cuphead is a classic run and gun adventure that heavily emphasizes on boss battles.</p><p>Play as Cuphead or his brother Mugman that signed a deal with the devil and know needs to bring the master souls of its debtors. Players can go through the campaign single-handedly or with a friend using built-in co-op capabilities.</p><p>The backgrounds, characters and other miscellaneous staff were painstakingly created using the methods from the era it mimics; Every character and every scene were hand-drawn to mimic vintage American cartoons. Each note in the orchestral soundtrack was composed with unparalleled attention to detail. Cuphead is an addictive mix of bone-crushing difficulty, fun and audiovisual feast that instantly makes you crave for more.</p>') ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(28154, 4) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(28154, 1) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(28154, 7) ON CONFLICT DO NOTHING;
INSERT INTO releases(game, region, release_date) VALUES(28154, 1, '2017-09-29') ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(28154, 83) ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(28154, 4) ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(28154, 51) ON CONFLICT DO NOTHING;
INSERT INTO development(game, developer) VALUES(28154, 13799) ON CONFLICT DO NOTHING;
INSERT INTO development(game, developer) VALUES(28154, 121012) ON CONFLICT DO NOTHING;
INSERT INTO publishing(game, publisher) VALUES(28154, 11164) ON CONFLICT DO NOTHING;
INSERT INTO publishing(game, publisher) VALUES(28154, 33005) ON CONFLICT DO NOTHING;
-- BOTW - Multiplatform - Otro genero
INSERT INTO games(game, title, cover, description) VALUES(22511, 'The Legend of Zelda: Breath of the Wild',
														  'https://kapodaco.files.wordpress.com/2017/04/loz-botw-1.jpg?w=533&h=650',
														  '<p>The Legend of Zelda: Breath of the Wild is an adventure game developed by Nintendo. It is the nineteenth installment in the series.</p><p>After awakening from a hundred year sleep, memoryless Link hears a mysterious female voice that guides him to a destroyed kingdom of Hyrule. He finds a Wiseman who says that a ruthless creature, Calamity Ganon, was imprisoned for 100 years. Even though the creature is trapped, it is still gaining power. Link sets out to kill Ganon before he frees himself and destroys the world.</p><p>In contrast to the previous titles in the series, Breath of the Wild the player to explore a vast open world. At the beginning of the game, a small tutorial is given to the players and they are free to travel the world at the pace they see fit. Link can climb almost every surface in the world, search for new equipment, cook food to restore health. Fast travel to certain places in the world is also available for the players. The world is highly interactive. For example, trees can be chopped down in order to make fire, or Link can use his shield as a snowboard.</p>') ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(22511, 10) ON CONFLICT DO NOTHING;
INSERT INTO game_versions(game, platform) VALUES(22511, 7) ON CONFLICT DO NOTHING;
INSERT INTO releases(game, region, release_date) VALUES(22511, 1, '2017-03-02') ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(22511, 4) ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(22511, 3) ON CONFLICT DO NOTHING;
INSERT INTO classifications(game, genre) VALUES(22511, 5) ON CONFLICT DO NOTHING;
INSERT INTO development(game, developer) VALUES(22511, 16257) ON CONFLICT DO NOTHING;
INSERT INTO publishing(game, publisher) VALUES(22511, 10681) ON CONFLICT DO NOTHING;