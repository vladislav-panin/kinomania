package com.kinoafisha.siteKino.repository;


import com.kinoafisha.siteKino.model.FilmModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<FilmModel, Integer> {

    List<FilmModel> findAll();


    FilmModel findFilmModelByFilmId(Integer id);

    FilmModel findFilmModelByName(String name);
}
