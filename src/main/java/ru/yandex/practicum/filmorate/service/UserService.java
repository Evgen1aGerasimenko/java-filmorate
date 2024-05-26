package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> usersFriends(Long id);

    List<User> commonFriends(Long id, Long otherId);
}
