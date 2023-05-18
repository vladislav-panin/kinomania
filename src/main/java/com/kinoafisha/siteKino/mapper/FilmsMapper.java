package com.kinoafisha.siteKino.mapper;

import com.kinoafisha.siteKino.model.CommentsModel;
import com.kinoafisha.siteKino.model.FilmModel;
import com.kinoafisha.siteKino.model.dto.CommentsShortDto;
import com.kinoafisha.siteKino.model.dto.FilmFullDto;
import com.kinoafisha.siteKino.model.dto.FilmsShortDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FilmsMapper {
    private final ModelMapper modelMapper;




    public FilmFullDto toFilmFullDto(FilmModel filmModel) {
        List<CommentsModel> commentsModelList = filmModel.getComments();
        List<CommentsShortDto> commentsShortDtoList = new ArrayList<>();
        for(CommentsModel commentsModel : commentsModelList ) {
            CommentsShortDto oneShortDto = modelMapper.map(commentsModelList, CommentsShortDto.class);
            commentsShortDtoList.add(oneShortDto);
        }
        FilmFullDto filmFullDto = modelMapper.map(filmModel, FilmFullDto.class);

//Нужно ли тут сделать filmFullDto.setCommentsShortDtos(commentsShortDtoList), или они итак добавились?

        return Objects.isNull(filmModel) ? null : filmFullDto;

    }

    //Это так и будет
    public FilmsShortDto toFilmsShortDto(FilmModel filmModel) {
        return Objects.isNull(filmModel) ? null :
                modelMapper.map(filmModel, FilmsShortDto.class);
    }
}