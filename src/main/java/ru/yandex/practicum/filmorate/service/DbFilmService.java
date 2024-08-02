package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenresRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenresRepository genresRepository;

    @Override
    public List<Film> getFilms() {
        log.info("Получен запрос для получения списка фильмов");
        return filmRepository.getFilms();
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос для добавления нового фильма" + film);
        if (film.getGenres() != null) {
            for (Genres genres : film.getGenres()) {
                if (genresRepository.getGenreById(genres.getId()) == null) {
                    throw new ValidationException("Пользователь не найден");
                }
            }
        }
        mpaRepository.getMpaById(film.getMpa().getId());
        return filmRepository.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос для изменения фильма" + film);
        Long filmId = film.getId();
        if (filmId == null) {
            throw new ValidationException("Ошибка изменения данных фильма. Не задан идентификатор фильма для обновления.");
        }
        filmRepository.getFilmById(film.getId());
        return filmRepository.updateFilm(film);
    }

    @Override
    public void addLike(Long id, Long userId) {
        log.info("Получен запрос для добавления лайка");
        userRepository.getUserById(userId);
        Film film = filmRepository.getFilmById(id);
        if (film.getLikes().contains(userId)) {
            throw new ValidationException("Пользователь уже поставил лайк этому фильму");
        }
        filmRepository.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.info("Получен запрос для удаления лайка");
        userRepository.getUserById(userId);
        filmRepository.deleteLike(id, userId);
    }

    @Override
    public List<Film> getTheTopFilms(int count) {
        log.info("Получен запрос на получение списка лучших фильмов");
        return filmRepository.getTheTopFilms(count);
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("Получен запрос на получение фильма");
        return filmRepository.getFilmById(id);
    }
}