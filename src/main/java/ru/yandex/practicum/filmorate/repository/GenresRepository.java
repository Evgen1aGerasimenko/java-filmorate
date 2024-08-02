package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenresRepository {
    List<Genres> getGenres();

    Genres getGenreById(int id);
}
