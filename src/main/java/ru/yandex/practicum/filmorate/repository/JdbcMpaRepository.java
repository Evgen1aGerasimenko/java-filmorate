package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.Mapper.MpaMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final MpaMapper mpaMapper;

    @Override
    public List<Mpa> getMpa() {
        String sql = "SELECT * FROM mpa";
        List<Mpa> mpaList = jdbc.query(sql, mpaMapper);
        return mpaList;
    }

    @Override
    public Mpa getMpaById(int id) {
        try {
            String sql = "SELECT * FROM mpa WHERE mpa_id = :mpa_id";
            Mpa mpa = jdbc.queryForObject(sql, new MapSqlParameterSource("mpa_id", id), mpaMapper);
            return mpa;
        } catch (Exception e) {
            throw new ValidationException("Задан некорректный рейтинг для фильма");
        }
    }
}
