package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    @Past
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
