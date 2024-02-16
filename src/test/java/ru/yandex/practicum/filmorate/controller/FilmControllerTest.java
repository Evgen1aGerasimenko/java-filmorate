package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FilmControllerTest {
    private Film film;
    private FilmController filmController;


    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = new Film();
        film.setName("New film");
        film.setDescription("This film is about ...");
        film.setReleaseDate(LocalDate.of(2017, 12, 28));
        film.setDuration(120);
    }

    @Test
    void shouldGetAllFilms() {
        filmController.createFilm(film);
        assertEquals(1, filmController.getFilms().size(), "Список не должен быть пустым");
    }

    @Test
    void shouldCreateFilm() {
        assertEquals(0, filmController.getFilms().size(), "Список должен быть пустым");
        filmController.createFilm(film);
        assertEquals(1, filmController.getFilms().size(), "Фильм не был создан");
    }

    @Test
    void shouldUpdateFilm() {
        filmController.createFilm(film);
        Film film1 = new Film();
        film1.setId(1);
        film1.setName("New filmUpdation");
        film1.setDescription("This film is about ...updated");
        film1.setReleaseDate(LocalDate.of(2020, 10, 25));
        film1.setDuration(180);

        filmController.updateFilm(film1);

        assertEquals("New filmUpdation", film.getName(), "Имя фильма не обновилось");
        assertEquals("This film is about ...updated", film.getDescription(), "Описание фильма не обновилось");
        assertEquals(LocalDate.of(2020, 10, 25), film.getReleaseDate(), "Дата релиза не обновилась");
        assertEquals(180, film.getDuration(), "Продолжительность не обновилась");

    }
}
