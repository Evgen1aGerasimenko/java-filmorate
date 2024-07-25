package ru.yandex.practicum.filmorate.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.Mapper.UserMapper;

import java.util.*;

@Repository
@Getter
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final UserMapper userMapper;

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        List<User> usersList = jdbc.query(sql, userMapper);
        return usersList;
    }

    @Override
    public User createUser(User user) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("name", user.getName()).addValue("email", user.getEmail()).addValue("login", user.getLogin()).addValue("birthday", user.getBirthday());
        String sql = "INSERT INTO users (name, email, login, birthday) VALUES (:name, :email, :login, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, params, keyHolder, new String[]{"user_id"});
        user.setId(keyHolder.getKeyAs(Long.class));
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET name = :name, email = :email, login = :login, birthday = :birthday " + "WHERE user_id = :user_id";
        SqlParameterSource params = new MapSqlParameterSource().addValue("name", user.getName()).addValue("email", user.getEmail()).addValue("login", user.getLogin()).addValue("birthday", user.getBirthday()).addValue("user_id", user.getId());
        jdbc.update(sql, params);
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (:user_id, :friend_id)";
        jdbc.update(sql, Map.of("user_id", id, "friend_id", friendId));
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id";
        jdbc.update(sql, Map.of("user_id", id, "friend_id", friendId));
    }

    @Override
    public List<User> usersFriends(Long id) {
        String sql = "SELECT * FROM users AS u, friends AS f " + "WHERE u.user_id = f.friend_id AND f.user_id = :user_id";
        List<User> list = jdbc.query(sql, Map.of("user_id", id), userMapper);
        return list;
    }

    @Override
    public List<User> commonFriends(Long id, Long otherId) {
        String sql = " SELECT * FROM users WHERE user_id in (SELECT friend_id FROM friends " +
                " WHERE friend_id in(SELECT friend_id FROM friends WHERE user_id = :user_id) " +
                "AND user_id = :friend_id);";
        List<User> commonFriends = jdbc.query(sql, Map.of("user_id", id, "friend_id", otherId), userMapper);
        return commonFriends;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = :user_id";
        try {
            User user = jdbc.queryForObject(sql, new MapSqlParameterSource("user_id", id), userMapper);

            String sqlFriends = "SELECT COUNT(user_id) FROM friends WHERE user_id = :user_id";
            Long friendsCount = jdbc.queryForObject(sqlFriends, Map.of("user_id", user.getId()), Long.class);
            user.setFriends(Collections.singleton(friendsCount));
            return user;
        } catch (Exception e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
