package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
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
public class FilmController extends Film {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос для получения списка фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос для добавления нового фильма");
        int id = ++idGenerator;
        film.setId(id);
        films.put(id, film);
        log.info("Новый фильм добавлен " + film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException {
        log.info("Получен запрос для изменения фильма");
        Integer filmId = film.getId();
        if (filmId == null) {
            throw new ValidationException("Ошибка изменения данных фильма. Не задан идентификатор фильма для обновления.");
        }
        Film filmUpdate = films.get(film.getId());
        if (filmUpdate == null) {
            throw new NotFoundException("Неизвестная ошибка");
        }
        filmUpdate.setName(film.getName());
        filmUpdate.setDescription(film.getDescription());
        filmUpdate.setReleaseDate(film.getReleaseDate());
        filmUpdate.setDuration(film.getDuration());
        log.info("Фильм изменен " + film);
        return film;
    }
}
