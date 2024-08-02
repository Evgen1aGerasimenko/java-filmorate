package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.repository.Mapper.MpaMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@Import({JdbcMpaRepository.class, MpaMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaRepositoryTest {

    @Autowired
    private final JdbcMpaRepository mpaRepository;

    @Test
    void should_check_getting_all_genres() {
        assertEquals(5, mpaRepository.getMpa().size(), "Список не должен быть пустым");
    }

    @Test
    void should_get_mpa_by_id() {
        assertEquals("G", mpaRepository.getMpaById(1).getName(),
                "Неверное название рейтинга");
    }

}
