package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {

    private UserStorage userStorage;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<User> getUsers() {
        log.info("Получен запрос для получения списка всех пользователей");
        return userStorage.getUsers();
    }

    @Override
    public User createUser(User user) {
        log.info("Получен запрос для создания нового пользователя");
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Получен запрос для изменения данных пользователя" + user.getId());
        return userStorage.updateUser(user);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        userStorage.addFriend(id, friendId);
        log.info("Пользователи " + id + " и " + friendId + " стали друзьями");
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        userStorage.deleteFriend(id, friendId);
        log.info("Пользователи " + id + " и " + friendId + " больше не друзья");
    }

    @Override
    public List<User> usersFriends(Long id) {
        return userStorage.usersFriends(id);
    }

    @Override
    public List<User> commonFriends(Long id, Long otherId) {
        return userStorage.commonFriends(id, otherId);
    }
}
