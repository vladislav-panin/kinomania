package com.kinoafisha.siteKino.repository;

import com.kinoafisha.siteKino.model.RatingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingModel, Integer> {
    RatingModel findRatingModelByFilmIdAndUserId(Integer filmId, Integer userId);

    List<RatingModel> findRatingModelsByRatingAndUserId(Integer rating, Integer userId);

    List<RatingModel> findRatingModelsByFilmId(Integer filmId);
}
