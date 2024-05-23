package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final UserStorage userStorage;

    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private final Map<Long, Film> films = new HashMap<>();
    private long idGenerator = 0L;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        long id = ++idGenerator;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Long filmId = film.getId();
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
        return film;
    }

    @Override
    public void addLike(Long id, Long userId) {
        userStorage.getUserId(userId);
        Film film = getFilmId(id);
        if (film.getLikes().contains(userId)) {
            throw new ValidationException("Пользователь уже поставил лайк этому фильму");
        } else {
            film.getLikes().add(userId);
        }
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        userStorage.getUserId(userId);
        getFilmId(id).getLikes().remove(userId);
    }

    @Override
    public List<Film> getTheTopFilms(Long count) {
        return getFilms().stream()
                .sorted((o, o1) -> o1.getLikes().size() - o.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Film getFilmId(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }
}
