package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.repository.Mapper.GenresMapper;


import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@Import({JdbcGenresRepository.class, GenresMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenresRepositoryTest {
    @Autowired
    private final JdbcGenresRepository genresRepository;

    @Test
    void should_check_getting_all_genres() {
        assertEquals(6, genresRepository.getGenres().size(), "Список не должен быть пустым");
    }

    @Test
    void should_get_genre_by_id() {
        assertEquals("Комедия", genresRepository.getGenreById(1).getName(),
                "Неверное название жанра");
    }
}
