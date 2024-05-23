package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmStorageTest {
    private Film film;
    private FilmStorage filmStorage;
    private static Validator validator;
    private UserStorage userStorage;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @BeforeEach
    public void beforeEach() {
        filmStorage = new InMemoryFilmStorage(userStorage);
        film = new Film();
        film.setName("New film");
        film.setDescription("This film is about ...");
        film.setReleaseDate(LocalDate.of(2017, 12, 28));
        film.setDuration(120);
    }

    @Test
    void shouldGetAllFilms() {
        filmStorage.createFilm(film);
        assertEquals(1, filmStorage.getFilms().size(), "Список не должен быть пустым");
    }

    @Test
    void shouldCreateFilm() {
        assertEquals(0, filmStorage.getFilms().size(), "Список должен быть пустым");
        filmStorage.createFilm(film);
        assertEquals(1, filmStorage.getFilms().size(), "Фильм не был создан");
    }

    @Test
    void shouldUpdateFilm() {
        filmStorage.createFilm(film);
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("New filmUpdation");
        film1.setDescription("This film is about ...updated");
        film1.setReleaseDate(LocalDate.of(2020, 10, 25));
        film1.setDuration(180);

        filmStorage.updateFilm(film1);

        assertEquals("New filmUpdation", film.getName(), "Имя фильма не обновилось");
        assertEquals("This film is about ...updated", film.getDescription(), "Описание фильма не обновилось");
        assertEquals(LocalDate.of(2020, 10, 25), film.getReleaseDate(), "Дата релиза не обновилась");
        assertEquals(180, film.getDuration(), "Продолжительность не обновилась");
    }

    @Test
    void shouldValidateIncorrectName() {
        film.setName(" ");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Название фильма должно оставаться пустым для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectDuration() {
        film.setDuration(-120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Продолжительность фильма должна быть отрицательной для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectDescription() {
        film.setDescription("This film is about ...Alice in Wonderland is a 1951" +
                " American animated musical fantasy comedy film produced by Walt " +
                "Disney Productions and released by RKO Radio Pictures. " +
                "It is based on Lewis Carroll's 1865 novel Alice's Adventures in " +
                "Wonderland");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Описание фильма должно быть более 200 знаков для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectReleaseDate() {
        film.setReleaseDate(LocalDate.of(1017, 12, 28));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Дата релиза должна быть ранее 28/12/1895 для прохождения теста");
    }

    @Test
    void correctFilmValidation() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "Неверно заданы параметы фильма");
    }
}
