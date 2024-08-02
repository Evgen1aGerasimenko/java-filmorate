package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.Mapper.UserMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase
@Import({JdbcUserRepository.class, UserMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {

    @Autowired
    private final JdbcUserRepository userRepository;

    @Test
    void should_get_user_by_id() {
        assertEquals("John", userRepository.getUserById(1L).getName(),
                "Неверное имя пользователя");
        assertEquals("email@dog.com", userRepository.getUserById(1L).getEmail(),
                "Неверный емайл пользователя");
        assertEquals("frog", userRepository.getUserById(1L).getLogin(),
                "Неверный логин пользователя");
        assertEquals(LocalDate.of(2002, 12, 12), userRepository.getUserById(1L).getBirthday(),
                "Неверная дата рождения пользователя");
    }

    @Test
    void should_check_getting_all_users() {
        assertEquals(3, userRepository.getUsers().size(), "Список не должен быть пустым");
    }

    @Test
    void should_check_the_user_was_created() {
        assertEquals(1, userRepository.getUserById(1L).getId(), "Пользователь не был создан");
    }

    @Test
    void should_check_update_user() {
        assertEquals("John3", userRepository.getUserById(3L).getName(),
                "Имя пользователя не обновилось");
        assertEquals("email@dog3.com", userRepository.getUserById(3L).getEmail(),
                "Емайл пользователя не обновился");
        assertEquals("frog3", userRepository.getUserById(3L).getLogin(),
                "Логин пользователя не обновился");
        assertEquals(LocalDate.of(2001, 12, 14), userRepository.getUserById(3L).getBirthday(),
                "Дата рождения пользователя не обновилась");
    }

    @Test
    void should_check_added_friend() {
        assertEquals(Collections.singleton(1L), userRepository.getUserById(2L).getFriends(),
                "Неверное друзей у пользователя 2");
    }

    @Test
    void should_check_deleted_friend() {
        assertEquals(Collections.singleton(1L), userRepository.getUserById(1L).getFriends(),
                "Неверное друзей у пользователя 1");
    }

    @Test
    void should_check_usersFriends() {
        assertTrue(userRepository.getUserById(2L).getFriends().size() == 1,
                "У пользователя 2 должен быть 1 друг");
    }

    @Test
    void should_check_commonFriends() {
        User user1 = userRepository.getUserById(1L);
        User user2 = userRepository.getUserById(2L);
        Set<Long> commonFriends = user1.getFriends().stream()
                .filter(user2.getFriends()::contains)
                .collect(Collectors.toSet());
        assertTrue(commonFriends.size() > 0, "У пользователей должен быть общий друг");
    }
}
