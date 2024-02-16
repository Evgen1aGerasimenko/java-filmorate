package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validators.ReleaseDateConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@NoArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
}
