package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.repository.Mapper.GenresMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcGenresRepository implements GenresRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final GenresMapper genresMapper;

    @Override
    public List<Genres> getGenres() {
        String sql = "SELECT * FROM genres";
        List<Genres> genres = jdbc.query(sql, genresMapper);
        return genres;
    }

    @Override
    public Genres getGenreById(int id) {
        try {
            String sql = "SELECT * FROM genres WHERE genre_id = :genre_id";
            Genres genre = jdbc.queryForObject(sql, new MapSqlParameterSource("genre_id", id), genresMapper);
            return genre;
        } catch (Exception e) {
            throw new ValidationException("Жанр не найден в списке");
        }
    }
}
