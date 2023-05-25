package com.kinoafisha.siteKino.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comments_table")
@Data
public class CommentsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer commentsId;

    String name; // имя юзера

    String message; //  отзыв

    String image; // ссылка на аватарку

    String filmName;

}
