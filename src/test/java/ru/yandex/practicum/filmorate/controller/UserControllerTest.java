package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    private User user;
    private UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        user = new User();
        user.setName("UserName");
        user.setLogin("UserLogin");
        user.setBirthday(LocalDate.of(2000,02,20));
        user.setEmail("user@ya.com");
    }

    @Test
    void shouldGetAllUsers() {
        userController.createUser(user);
        assertEquals(1, userController.getUsers().size(), "Список должен содержать одного пользователя");

    }

    @Test
    void shouldCreateNewUser() {
        assertEquals(0, userController.getUsers().size(), "Список должен быть пустым");
        userController.createUser(user);
        assertEquals(1, userController.getUsers().size(), "Пользователь не создан");

    }

    @Test
    void shouldUpdateUser() {
        userController.createUser(user);

        User user1 = new User();
        user1.setName("UserNameUpdated");
        user1.setLogin("UserLoginUpdated");
        user1.setBirthday(LocalDate.of(1990,02,20));
        user1.setEmail("userUpdated@ya.com");
        user1.setId(1);

        userController.updateUser(user1);

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
        user1.setBirthday(LocalDate.of(2000,02,20));
        user1.setEmail("user@ya.com");

        userController.createUser(user1);

        assertEquals(user1.getName(), user1.getLogin(), "Логин должен быть присвоен как имя пользователя");
    }
}
