package ru.yandex.practicum.filmorate.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
    @Constraint(validatedBy = ReleaseDataValidator.class)
    @Target( { ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ReleaseDateConstraint {
        String message() default "Film release data must be after 28.12.1895";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

}
