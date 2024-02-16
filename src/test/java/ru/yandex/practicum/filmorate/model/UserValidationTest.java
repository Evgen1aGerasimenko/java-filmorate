package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidationTest {

    private User user;

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @BeforeEach
    void beforeEach() {
        user = new User();
    }

    @Test
    void shouldValidateIncorrectEmailWithoutTheEtOrBlank() {
        user.setEmail("");
        user.setLogin("first");
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
        user.setEmail("user@ya.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Логин должен быть пустым для прохождения теста");
    }

    @Test
    void shouldValidateIncorrectBirthDay() {
        user.setLogin("user");
        user.setEmail("user@ya.com");
        user.setBirthday(LocalDate.of(2100,10,20));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Дата рождения должна быть в будущем для прохождения теста");
    }

    @Test
    void correctUserValidation() {
        user.setName("User");
        user.setLogin("UserLogin");
        user.setEmail("user@ya.com");
        user.setBirthday(LocalDate.of(1990,10,20));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "Неверно заданы параметы пользователя");

    }
}
