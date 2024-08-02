package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.Mapper.FilmMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.repository.Mapper.GenresMapper;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final FilmMapper filmMapper;
    private final GenresMapper genresMapper;

    @Override
    public List<Film> getFilms() {

        String sql = "SELECT f.film_id," +
                " f.name," +
                " f.description," +
                " f.releaseDate," +
                " f.duration," +
                " m.mpa_id," +
                " m.name," +
                " COUNT(l.user_id) AS likes," +
                " fg.genre_id," +
                " g.name AS genres" +
                " FROM films f" +
                " JOIN mpa m ON f.mpa = m.mpa_id" +
                " LEFT JOIN likes l ON f.film_id = l.film_id" +
                " LEFT JOIN film_genre fg ON f.film_id = fg.film_id" +
                " LEFT JOIN genres g ON fg.genre_id = g.genre_id" +
                " GROUP BY f.film_id, f.name, f.description, f.releaseDate, f.duration, f.mpa, f.likes, fg.genre_id, g.name";
        List<Film> films = jdbc.query(sql, filmMapper);
        for (Film film : films) {
            film.setGenres(new HashSet<>(jdbc.query(sql, genresMapper)));
        }
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films (name, description, duration, releaseDate, mpa)" +
                " VALUES (:name, :description, :duration, :releaseDate, :mpa_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("duration", film.getDuration())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("mpa_id", film.getMpa().getId());
        jdbc.update(sql, mapSqlParameterSource, keyHolder, new String[]{"film_id"});
        film.setId(keyHolder.getKeyAs(Long.class));
        addGenres(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name = :name, description = :description, duration = :duration," +
                " releaseDate = :releaseDate, mpa = :mpa_id WHERE film_id = :film_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("duration", film.getDuration())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("mpa_id", film.getMpa().getId())
                .addValue("film_id", film.getId());
        jdbc.update(sql, params);
        String sqlDelete = "DELETE FROM film_genre WHERE film_id = :id";
        jdbc.update(sqlDelete, Map.of("id", film.getId()));
        addGenres(film);
        return film;
    }

    @Override
    public void addLike(Long id, Long userId) {
        String sql = "MERGE INTO likes (film_id, user_id) VALUES (:id, :userId)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userId", userId);
        jdbc.update(sql, params);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        String sql = "DELETE FROM likes WHERE film_id = :id AND user_id = :userId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("userId", userId);
        jdbc.update(sql, params);
    }

    @Override
    public List<Film> getTheTopFilms(int count) {
        String sql = "SELECT f.film_id," +
                " f.name," +
                " f.description," +
                " f.releaseDate," +
                " f.duration," +
                " m.mpa_id," +
                " m.name," +
                " COUNT(l.user_id) AS likes," +
                " fg.genre_id," +
                " g.name as genres" +
                " FROM films f" +
                " LEFT JOIN mpa m ON f.mpa = m.mpa_id" +
                " LEFT JOIN likes AS l ON f.film_id = l.film_id" +
                " LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id" +
                " LEFT JOIN genres AS g ON fg.genre_id = g.genre_id" +
                " GROUP BY f.film_id, f.name, f.description, f.releaseDate, f.duration, f.mpa, f.likes, fg.genre_id, g.name" +
                " ORDER BY COUNT(l.user_id) DESC " +
                " LIMIT :count";
        List<Film> films = jdbc.query(sql, Map.of("count", count), filmMapper);
        for (Film film : films) {
            film.setGenres(new HashSet<>(jdbc.query(sql, Map.of("count", count), genresMapper)));
        }
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT f.film_id," +
                " f.name," +
                " f.description," +
                " f.releaseDate," +
                " f.duration," +
                " m.mpa_id," +
                " m.name," +
                " COUNT(l.user_id) AS likes," +
                " fg.genre_id," +
                " g.name as genres" +
                " FROM films f" +
                " LEFT JOIN mpa m ON f.mpa = m.mpa_id" +
                " LEFT JOIN likes AS l ON f.film_id = l.film_id" +
                " LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id" +
                " LEFT JOIN genres AS g ON fg.genre_id = g.genre_id" +
                " WHERE f.film_id = :id" +
                " GROUP BY f.film_id, f.name, f.description, f.releaseDate, f.duration, f.mpa, f.likes, fg.genre_id, g.name";
        try {
            Film film = jdbc.queryForObject(sql, Map.of("id", id), new RowMapper<Film>() {
                @Override
                public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                    final Film film = new Film();
                    film.setId(rs.getLong("film_id"));
                    film.setName(rs.getString("name"));
                    film.setDescription(rs.getString("description"));
                    film.setDuration(rs.getInt("duration"));
                    film.setReleaseDate((rs.getTimestamp("releaseDate")).toLocalDateTime().toLocalDate());
                    film.setMpa(new Mpa(rs.getInt("mpa.mpa_id"), rs.getString("mpa.name")));
                    film.setLikes(Collections.singleton(rs.getLong("likes")));
                    film.setGenres(new HashSet<>());
                    do {
                        if (rs.getString("genres") != null) {
                            final Genres genre = new Genres(rs.getInt("genre_id"), rs.getString("genres"));
                            film.getGenres().add(genre);
                        }
                    } while (rs.next());
                    return film;
                }
            });
            return film;
        } catch (Exception e) {
            throw new NotFoundException("Фильм не найден");
        }
    }

    private void addGenres(Film film) {
        String sqlGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        List<Integer> genres = film.getGenres()
                .stream()
                .map(s -> s.getId())
                .collect(Collectors.toList());

        jdbc.getJdbcOperations().batchUpdate(sqlGenre, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film.getId());
                ps.setInt(2, genres.get(i));
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }
}