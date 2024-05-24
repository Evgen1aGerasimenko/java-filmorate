package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InMemoryFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final UserStorage userStorage;

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
        Long filmId = film.getId();
        if (filmId == null) {
            throw new ValidationException("Ошибка изменения данных фильма. Не задан идентификатор фильма для обновления.");
        }
        filmStorage.getFilmId(film.getId());
        return filmStorage.updateFilm(film);
    }

    @Override
    public void addLike(Long id, Long userId) {
        log.info("Получен запрос для добавления лайка");
        userStorage.getUserId(userId);
        Film film = filmStorage.getFilmId(id);
        if (film.getLikes().contains(userId)) {
            throw new ValidationException("Пользователь уже поставил лайк этому фильму");
        }
        filmStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.info("Получен запрос для удаления лайка");
        userStorage.getUserId(userId);
        filmStorage.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTheTopFilms(Long count) {
        log.info("Получен запрос на получение списка лучших фильмов");
        return filmStorage.getTheTopFilms(count);
    }
}

