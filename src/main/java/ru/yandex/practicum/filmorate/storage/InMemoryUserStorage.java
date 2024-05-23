package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
public class InMemoryUserStorage implements UserStorage {
    protected final Map<Long, User> users = new HashMap<>();
    private long idGenerator = 0L;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        long id = ++idGenerator;
        user.setId(id);
        users.put(id, user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long userId = user.getId();
        if (userId == null) {
            throw new ValidationException("Ошибка изменения данных пользователя. Не задан идентификатор пользователя для обновления.");
        }
        User userUpdate = users.get(user.getId());
        if (users.get(userId) == null) {
            throw new NotFoundException("Неизвестная ошибка");
        }
        userUpdate.setName(user.getName());
        userUpdate.setLogin(user.getLogin());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setBirthday(user.getBirthday());
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        getUserId(id).getFriends().add(friendId);
        getUserId(friendId).getFriends().add(id);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        getUserId(id).getFriends().remove(friendId);
        getUserId(friendId).getFriends().remove(id);
    }

    @Override
    public List<User> usersFriends(Long id) {
        User user = getUserId(id);
        return user.getFriends().stream()
                .map(this::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> commonFriends(Long id, Long otherId) {
        Set<Long> commonFriends = new HashSet<>(getUserId(id).getFriends());
        commonFriends.retainAll(getUserId(otherId).getFriends());
        return commonFriends.stream()
                .map(this::getUserId)
                .collect(Collectors.toList());
    }

    public User getUserId(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }
}
