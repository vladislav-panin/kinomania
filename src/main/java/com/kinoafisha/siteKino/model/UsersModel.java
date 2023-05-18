package com.kinoafisha.siteKino.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users_table")
@Data
public class UsersModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer userId;

    String login;

    String password;

    //String image - пока нет и мб не будет

    Integer authentificated;

    ///////////////////////////////////Новые поля, нужные для юзер профиля

    String preferences; // предпочтения

    String birthDate; //дата рождения


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    List<FilmModel> recomendedFilms; //лист рекомендаций


}
