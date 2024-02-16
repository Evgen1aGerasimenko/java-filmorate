package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 0;

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос для получения списка фильмов");
        try {
        return new ArrayList<>(films.values());
    } catch (Exception e) {
        log.error("Ошибка получения списка всех фильмов");
        throw new ValidationException(e);
    }
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос для добавления нового фильма");
        try {
            int id = ++idGenerator;
            film.setId(id);
            films.put(id, film);
        log.info("Новый фильм добавлен " + film);
        return film;
    } catch (Exception e) {
        log.error("Ошибка добавления фильма");
        throw new ValidationException(e);
    }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос для изменения фильма");
        try {
        Film filmUpdate = films.get(film.getId());
        filmUpdate.setName(film.getName());
        filmUpdate.setDescription(film.getDescription());
        filmUpdate.setReleaseDate(film.getReleaseDate());
        filmUpdate.setDuration(film.getDuration());
        log.info("Фильм изменен " + film);
        return film;
    } catch (Exception e) {
        log.error("Ошибка изменения данных фильма");
        throw new ValidationException(e);
    }
    }
}
