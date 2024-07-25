DROP TABLE IF EXISTS USERS CASCADE;
CREATE TABLE IF NOT EXISTS USERS
(
    user_id  BIGINT auto_increment primary key,
    name     VARCHAR(70),
    email    VARCHAR(200) NOT NULL,
    login    VARCHAR(70)  NOT NULL,
    birthday DATE
    );

DROP TABLE IF EXISTS FRIENDS CASCADE;
CREATE TABLE IF NOT EXISTS FRIENDS
(
    user_id  BIGINT,
    friend_id BIGINT,
    CONSTRAINT fk_user_id FOREIGN KEY(user_id)
    REFERENCES USERS(user_id),
    CONSTRAINT fk_friend_id FOREIGN KEY(friend_id)
        REFERENCES USERS(user_id),
    CONSTRAINT pk_friends PRIMARY KEY (user_id, friend_id)
    );

DROP TABLE IF EXISTS MPA CASCADE;
CREATE TABLE IF NOT EXISTS MPA
(
    mpa_id  INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
    );

DROP TABLE IF EXISTS FILMS CASCADE;
CREATE TABLE IF NOT EXISTS FILMS
(
    film_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(70),
    description VARCHAR(200),
    releaseDate DATE,
    duration INTEGER,
    CONSTRAINT positive_duration CHECK(duration > 1),
    mpa INTEGER,
    likes INTEGER,
    CONSTRAINT fk_mpa FOREIGN KEY(mpa) REFERENCES MPA(mpa_id)
    );

DROP TABLE IF EXISTS LIKES CASCADE;
CREATE TABLE IF NOT EXISTS LIKES
(
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_film_id_likes FOREIGN KEY(film_id)
    REFERENCES FILMS(film_id),
    CONSTRAINT fk_user_id_likes FOREIGN KEY(user_id)
    REFERENCES USERS(user_id),
    CONSTRAINT pk_likes PRIMARY KEY(film_id, user_id)
    );

DROP TABLE IF EXISTS GENRES CASCADE;
CREATE TABLE IF NOT EXISTS GENRES
(
    genre_id  INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
    );

DROP TABLE IF EXISTS FILM_GENRE CASCADE;
CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    film_id  BIGINT NOT NULL,
    genre_id  INTEGER NOT NULL,
    CONSTRAINT fk_genre_id_first FOREIGN KEY(film_id)
    REFERENCES FILMS(film_id),
    CONSTRAINT fk_genre_id FOREIGN KEY(genre_id)
    REFERENCES GENRES(genre_id),
    CONSTRAINT pk_film_genres PRIMARY KEY (film_id, genre_id)
    );