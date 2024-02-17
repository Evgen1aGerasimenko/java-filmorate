package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {

    private int id;
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    @Past
    private LocalDate birthday;
}
