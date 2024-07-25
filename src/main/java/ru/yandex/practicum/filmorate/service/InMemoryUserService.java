package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InMemoryUserService implements UserService {

    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public List<User> getUsers() {
        log.info("Получен запрос для получения списка всех пользователей");
        return userRepository.getUsers();
    }

    @Override
    public User createUser(User user) {
        log.info("Получен запрос для создания нового пользователя" + user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Получен запрос для изменения данных пользователя " + user);
        userRepository.getUserById(user.getId());
        return userRepository.updateUser(user);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        userRepository.getUserById(id);
        userRepository.getUserById(friendId);
        userRepository.addFriend(id, friendId);
        log.info("Пользователи " + id + " и " + friendId + " стали друзьями");
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        userRepository.getUserById(id);
        userRepository.getUserById(friendId);
        userRepository.deleteFriend(id, friendId);
        log.info("Пользователи " + id + " и " + friendId + " больше не друзья");
    }

    @Override
    public List<User> usersFriends(Long id) {
        log.info("Получен запрос на получение !списка друзей пользователя с идентификатором - " + id);
        userRepository.getUserById(id);
        return userRepository.usersFriends(id);
    }

    @Override
    public List<User> commonFriends(Long id, Long otherId) {
        log.info("Получен запрос на получение !списка общих друзей пользователя с идентификатором - " + id +
                " и идентификатором - " + otherId);
        userRepository.getUserById(id);
        userRepository.getUserById(otherId);
        return userRepository.commonFriends(id, otherId);
    }
}

