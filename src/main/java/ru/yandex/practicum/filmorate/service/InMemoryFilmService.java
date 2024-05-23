package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {
    private FilmStorage filmStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);


    @Override
    public List<Film> getFilms() {
        log.info("Получен запрос для получения списка фильмов");
        return filmStorage.getFilms();
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос для добавления нового фильма" + film);
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос для изменения фильма" + film);
        return filmStorage.updateFilm(film);
    }

    @Override
    public void addLike(Long id, Long userId) {
        log.info("Получен запрос для добавления лайка");
        filmStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.info("Получен запрос для удаления лайка");
        filmStorage.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTheTopFilms(Long count) {
        log.info("Получен запрос на получение списка лучших фильмов");
        return filmStorage.getTheTopFilms(count);
    }
}

