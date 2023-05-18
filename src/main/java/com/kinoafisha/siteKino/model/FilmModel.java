package com.kinoafisha.siteKino.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "films_table")
@Data
public class FilmModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer filmId;

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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "filmId", referencedColumnName = "filmId")
    List<CommentsModel> comments;

    // СБРНИКОВ ПОКА НЕТУ
}
