package ru.yandex.practicum.filmorate.repository.Mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {

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
        return film;
    }
}

