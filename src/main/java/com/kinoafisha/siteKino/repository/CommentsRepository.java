package com.kinoafisha.siteKino.repository;

import com.kinoafisha.siteKino.model.CommentsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<CommentsModel, Integer> {

    List<CommentsModel> findAll();

    List<CommentsModel> findCommentsModelByFilmName(String filmName);

    CommentsModel findCommentsModelByCommentsId(Integer commentsId);
}
