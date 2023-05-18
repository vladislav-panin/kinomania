package com.kinoafisha.siteKino.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ratings_table")
@Data
public class RatingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ratingId;


    @Nullable
    Integer userId;

    @Nullable
    Integer filmId;

    @Nullable
    Integer rating;

    //табличка, чтобы искать какие юзеры какие фильмы оценили
}
