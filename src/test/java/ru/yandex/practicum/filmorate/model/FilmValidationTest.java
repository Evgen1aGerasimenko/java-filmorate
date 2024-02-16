package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmValidationTest {

    private Film film;

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @BeforeEach
    public void beforeEach() {
        film = new Film();
    }

    @Test
    void shouldValidateIncorrectName() {
        film.setName(" ");
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Название фильма должно оставаться пустым для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectDuration() {
        film.setName("Name");
        film.setDuration(-120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Продолжительность фильма должна быть отрицательной для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectDescription() {
        film.setName("Name");
        film.setDuration(120);
        film.setDescription("This film is about ...Alice in Wonderland is a 1951" +
                " American animated musical fantasy comedy film produced by Walt " +
                "Disney Productions and released by RKO Radio Pictures. " +
                "It is based on Lewis Carroll's 1865 novel Alice's Adventures in " +
                "Wonderland");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Описание фильма должно быть более 200 знаков для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectReliaseDate() {
        film.setName("Name");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1017, 12, 28));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Дата релиза должна быть ранее 28/12/1895 для прохождения теста");
    }

    @Test
    void correctFilmValidation() {
        film.setName("New film");
        film.setDescription("This film is about ...");
        film.setReleaseDate(LocalDate.of(2017, 12, 28));
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size(), "Неверно заданы параметы фильма");
    }
}
