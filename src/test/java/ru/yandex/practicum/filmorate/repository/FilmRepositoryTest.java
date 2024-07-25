package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.repository.Mapper.FilmMapper;
import ru.yandex.practicum.filmorate.repository.Mapper.GenresMapper;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({JdbcFilmRepository.class, FilmMapper.class, GenresMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryTest {
    @Autowired
    private final JdbcFilmRepository filmStorage;

    @Test
    void should_get_film_by_id() {
        assertEquals("New film", filmStorage.getFilmById(1L).getName(), "Неверное название фильма");
        assertEquals("This film is about ...", filmStorage.getFilmById(1L).getDescription(),
                "Неверное описание фильма");
        assertEquals(120, filmStorage.getFilmById(1L).getDuration(),
                "Неверная продолжительность фильма");
        assertEquals(LocalDate.of(2017, 12, 28), filmStorage.getFilmById(1L).getReleaseDate(),
                "Неверная дата релиза");
        assertEquals(1, filmStorage.getFilmById(1L).getMpa().getId(), "Неверный рейтинг фильма");
        assertEquals(2, filmStorage.getFilmById(1L).getGenres().size(),
                "Неверное количество жанров у фильма");
    }

    @Test
    void should_check_getting_all_films() {
        assertEquals(4, filmStorage.getFilms().size(), "Список не должен быть пустым");
    }

    @Test
    void should_check_the_film_was_created() {
        assertEquals(1, filmStorage.getFilmById(1L).getId(), "Фильм не был создан");
    }

    @Test
    void should_check_update_film() {

        assertEquals("updated", filmStorage.getFilmById(3L).getName(), "Имя фильма не обновилось");
        assertEquals("updated", filmStorage.getFilmById(3L).getDescription(),
                "Описание фильма не обновилось");
        assertEquals(LocalDate.of(2020, 10, 20), filmStorage.getFilmById(3L).getReleaseDate(),
                "Дата релиза не обновилась");
        assertEquals(200, filmStorage.getFilmById(3L).getDuration(),
                "Продолжительность не обновилась");
    }

    @Test
    void should_check_added_likes() {
        assertEquals(Collections.singleton(3L), filmStorage.getFilmById(2L).getLikes(),
                "Неверное количество лайков у пользователя 2");
    }

    @Test
    void should_check_deleted_likes() {
        assertEquals(Collections.singleton(2L), filmStorage.getFilmById(1L).getLikes(),
                "Неверное количество лайков у пользователя 1");

    }

    @Test
    void should_check_getting_the_top_films() {
        assertEquals(2, filmStorage.getTheTopFilms(2).size(),
                "Неверное получение топовых фильмов");
    }
}