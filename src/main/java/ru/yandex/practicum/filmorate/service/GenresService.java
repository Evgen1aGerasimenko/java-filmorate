package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenresService {
    List<Genres> getGenres();

    Genres getGenreById(int id);
}
