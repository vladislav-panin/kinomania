package com.kinoafisha.siteKino.service;

import com.kinoafisha.siteKino.mapper.RatingMapper;
import com.kinoafisha.siteKino.mapper.UsersMapper;
import com.kinoafisha.siteKino.model.RatingModel;
import com.kinoafisha.siteKino.model.dto.RatingDto;
import com.kinoafisha.siteKino.repository.RatingRepository;
import com.kinoafisha.siteKino.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    public RatingDto giveRatingToFilm(RatingModel ratingModel, Integer myRating){

        ratingModel.setRating(myRating);
        ratingRepository.save(ratingModel);
        RatingDto ratingDto = ratingMapper.toRatingDto(ratingModel);
        return ratingDto;

    }
}
