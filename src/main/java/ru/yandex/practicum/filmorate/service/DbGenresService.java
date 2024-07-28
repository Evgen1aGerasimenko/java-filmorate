package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.GenresController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.repository.GenresRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbGenresService implements GenresService {

    private static final Logger log = LoggerFactory.getLogger(GenresController.class);
    private final GenresRepository genresRepository;

    @Override
    public List<Genres> getGenres() {
        log.info("Получен запрос для получения списка жанров");
        return genresRepository.getGenres();
    }

    @Override
    public Genres getGenreById(int id) {
        log.info("Получен запрос для получения жанра с идентификатором : " + id);
        int genreSize = genresRepository.getGenres().size();
        if (genreSize < id) {
            throw new NotFoundException("Получен запрос с неверным идентификатором");
        }
        return genresRepository.getGenreById(id);
    }
}
