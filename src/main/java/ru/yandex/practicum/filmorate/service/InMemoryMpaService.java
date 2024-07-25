package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.MpaController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InMemoryMpaService implements MpaService {

    private static final Logger log = LoggerFactory.getLogger(MpaController.class);
    private final MpaRepository mpaRepository;

    @Override
    public List<Mpa> getMpa() {
        log.info("Получен запрос для получения возрастных категорий");
        return mpaRepository.getMpa();
    }

    @Override
    public Mpa getMpaById(int id) {
        log.info("Получен запрос для получения возрастной категории с идентификатором : " + id);
        int mpaSize = mpaRepository.getMpa().size();
        if (mpaSize < id) {
            throw new NotFoundException("Получен запрос с неверным идентификатором");
        }
        return mpaRepository.getMpaById(id);
    }
}
