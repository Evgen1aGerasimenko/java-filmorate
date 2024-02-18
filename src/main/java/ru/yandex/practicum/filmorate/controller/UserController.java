package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 0;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос для получения списка всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос для создания нового пользователя");
        int id = ++idGenerator;
        user.setId(id);
        users.put(id, user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Создан новый пользователь" + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws NotFoundException {
        log.info("Получен запрос для изменения данных пользователя");
        Integer userId = user.getId();
        if (userId == null) {
            throw new ValidationException("Ошибка изменения данных пользователя. Не задан идентификатор пользователя для обновления.");
        }
        User userUpdate = users.get(user.getId());
        if (userUpdate == null) {
            throw new NotFoundException("Неизвестная ошибка");
        }
        userUpdate.setName(user.getName());
        userUpdate.setLogin(user.getLogin());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setBirthday(user.getBirthday());
        log.info("Данные пользователя изменены" + user);
        return user;
    }
}
