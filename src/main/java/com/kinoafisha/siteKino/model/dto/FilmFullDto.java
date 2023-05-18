package com.kinoafisha.siteKino.model.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FilmFullDto {

    String name; // название фильма

    String description; // описание

    int year; // год выхода

    Date releaseDate; //полная дата выхода фильм

    String creator; //режиссер

    String time; // время сколько идет фильм

    String actors; //актеры

    String genre; // жанр

    @Nullable
    Integer rating; // рейтинг

    @Nullable
    Integer myRating; // Наш рейтинг

    List<CommentsShortDto> commentsShortDtos;
}