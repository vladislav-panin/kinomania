package com.kinoafisha.siteKino.mapper;

import com.kinoafisha.siteKino.model.RatingModel;
import com.kinoafisha.siteKino.model.UsersModel;
import com.kinoafisha.siteKino.model.dto.RatingDto;
import com.kinoafisha.siteKino.model.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingMapper {
    private final ModelMapper modelMapper;

    public RatingDto toRatingDto(RatingModel ratingModel)
    {
        return modelMapper.map(ratingModel, RatingDto.class);
    }
}
