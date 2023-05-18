package com.kinoafisha.siteKino.controller;

import com.kinoafisha.siteKino.model.CommentsModel;
import com.kinoafisha.siteKino.model.FilmModel;
import com.kinoafisha.siteKino.model.UsersModel;
import com.kinoafisha.siteKino.model.dto.CommentsShortDto;
import com.kinoafisha.siteKino.model.dto.FilmFullDto;
import com.kinoafisha.siteKino.model.dto.FilmsShortDto;
import com.kinoafisha.siteKino.repository.CommentsRepository;
import com.kinoafisha.siteKino.repository.FilmRepository;
import com.kinoafisha.siteKino.service.FilmsService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("films")
@RequiredArgsConstructor
public class FilmsController {

    private final FilmRepository filmRepository;
    private final CommentsRepository commentsRepository;

    private final FilmsService filmsService;

    @GetMapping("/easy")
    public String simpleReq(){
        return "easy";
    }



    //Поставить рейтинг и добавить фильм в рекомендации профиля


    @GetMapping("/all")
    public String getAll(Model model)// все фильмы
    {
        List<FilmsShortDto> filmsShortDtos = filmsService.getAllFilms();
        model.addAttribute("allFilms", filmsShortDtos);  //надо как-то запихать в шаблон фильмы. КАК ЖИ ЕНТО СДЕЛАТЬ?
        return "all_films";
    }

    //пока отдаю сам комментарий, мб вместо этого надо отдавать страницу с фильмом целиком?
    @PostMapping("/{filmId}/addComment")
    public String addCommentToFilm(@RequestBody String message, Model model) //добавление коментария к фильму
    {
        CommentsModel commentsModel = filmsService.addNewComment(message);
        model.addAttribute("comment", commentsModel);
        return "film_page";

    }



    //Все ниже методы переделать так, чтобы отправлять строку, а там из таймлива объект
    @GetMapping("/{filmId}")
    public FilmFullDto getFilmByFilmId(@PathVariable Integer filmId){ // получение фильма по айди

        return filmsService.findFilmById(filmId);

    }

    @GetMapping("/filmPage/{name}")
    public FilmFullDto getFilmByFilmName(@PathVariable String name){// получение фильма по имени
        return filmsService.findFilmByNameDto(name);

    }
}
