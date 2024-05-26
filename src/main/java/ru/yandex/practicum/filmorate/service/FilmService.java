package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    List<Film> getTheTopFilms(Long count);
}
