INSERT INTO users (name, email, login, birthday) VALUES ('John', 'email@dog.com', 'frog', '2002-12-12');
INSERT INTO users (name, email, login, birthday) VALUES ('John1', 'email@dog1.com', 'frog1', '2002-12-11');
INSERT INTO users (name, email, login, birthday) VALUES ('John2', 'email@dog2.com', 'frog2', '2002-12-10');

UPDATE users SET name = 'John3', email = 'email@dog3.com', login = 'frog3', birthday = '2001-12-14'
WHERE user_id = 3;

INSERT INTO friends (user_id, friend_id) VALUES (1, 2);
INSERT INTO friends (user_id, friend_id) VALUES (1, 3);
INSERT INTO friends (user_id, friend_id) VALUES (2, 3);

DELETE FROM friends WHERE user_id = 1 AND friend_id = 2;

INSERT INTO films (name, description, duration, releaseDate, mpa)
VALUES ('New film', 'This film is about ...', 120, '2017-12-28', 1);

INSERT INTO films (name, description, duration, releaseDate, mpa)
VALUES ('New film1', 'This film is about ...1', 121, '2017-12-29', 2);

INSERT INTO films (name, description, duration, releaseDate, mpa)
VALUES ('New film2', 'This film is about ...2', 122, '2017-12-30', 1);

INSERT INTO films (name, description, duration, releaseDate, mpa)
VALUES ('New film1', 'This film is about ...1', 121, '2017-12-29', 2);

UPDATE films SET name = 'updated', description = 'updated', duration = 200,
releaseDate = '2020-10-20', mpa = 2 WHERE film_id = 3;

MERGE INTO likes (film_id, user_id) VALUES (1, 1);
MERGE INTO likes (film_id, user_id) VALUES (1, 2);
MERGE INTO likes (film_id, user_id) VALUES (1, 3);

MERGE INTO likes (film_id, user_id) VALUES (2, 1);
MERGE INTO likes (film_id, user_id) VALUES (2, 2);
MERGE INTO likes (film_id, user_id) VALUES (2, 3);

DELETE FROM likes WHERE film_id = 1 AND user_id = 3;

INSERT INTO film_genre (film_id, genre_id) VALUES (1, 1);