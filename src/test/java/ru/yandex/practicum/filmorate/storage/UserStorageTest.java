package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserStorageTest {

    private User user;
    private UserStorage userStorage;
    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @BeforeEach
    void beforeEach() {
        userStorage = new InMemoryUserStorage();
        user = new User();
        user.setName("UserName");
        user.setLogin("UserLogin");
        user.setBirthday(LocalDate.of(2000, 02, 20));
        user.setEmail("user@ya.com");
    }

    @Test
    void shouldGetAllUsers() {
        userStorage.createUser(user);
        assertEquals(1, userStorage.getUsers().size(), "Список должен содержать одного пользователя");

    }

    @Test
    void shouldCreateNewUser() {
        assertEquals(0, userStorage.getUsers().size(), "Список должен быть пустым");
        userStorage.createUser(user);
        assertEquals(1, userStorage.getUsers().size(), "Пользователь не создан");
    }

    @Test
    void shouldUpdateUser() {
        userStorage.createUser(user);

        User user1 = new User();
        user1.setName("UserNameUpdated");
        user1.setLogin("UserLoginUpdated");
        user1.setBirthday(LocalDate.of(1990, 02, 20));
        user1.setEmail("userUpdated@ya.com");
        user1.setId(1L);

        userStorage.updateUser(user1);

        assertEquals(user1.getName(), user.getName(), "Имя пользователя не обновилось");
        assertEquals(user1.getLogin(), user.getLogin(), "Логин пользователя не обновился");
        assertEquals(user1.getBirthday(), user.getBirthday(), "Дата рождения пользователя не обновилась");
        assertEquals(user1.getEmail(), user.getEmail(), "Емайл пользователя не обновился");
    }

    @Test
    void shouldCheckCreationEmptyNameAndChangeItToLogin() {
        User user1 = new User();
        user1.setName("");
        user1.setLogin("UserLogin");
        user1.setBirthday(LocalDate.of(2000, 02, 20));
        user1.setEmail("user@ya.com");

        userStorage.createUser(user1);

        assertEquals(user1.getName(), user1.getLogin(), "Логин должен быть присвоен как имя пользователя");
    }

    @Test
    void shouldValidateIncorrectEmailWithoutTheEtOrBlank() {
        user.setEmail("");
        User user1 = new User();
        user1.setEmail("second.ya.com");
        user1.setLogin("second");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Электронная почта не должна содержать символов для прохождения теста");

        Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        assertEquals(1, violations1.size(), "Электронная почта не должна содержать символ @ для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectLogin() {
        user.setLogin("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Логин должен быть пустым для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectBirthDay() {
        user.setBirthday(LocalDate.of(2100, 10, 20));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Дата рождения должна быть в будущем для прохождения теста");
    }

    @Test
    void correctUserValidation() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "Неверно заданы параметы пользователя");
    }

    @Test
    void checkAddedFriends() {
        User user1 = userStorage.createUser(user);
        User user2 = userStorage.createUser(user);

        userStorage.addFriend(user1.getId(), user2.getId());
        assertTrue(user1.getFriends().contains(user2.getId()), "Должен содержать ид друга в сете");
        assertTrue(user2.getFriends().contains(user1.getId()), "Должен содержать ид друга в сете");
    }
}
