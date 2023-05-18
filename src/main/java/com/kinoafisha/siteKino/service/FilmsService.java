package com.kinoafisha.siteKino.service;

import com.kinoafisha.siteKino.mapper.CommentsMapper;
import com.kinoafisha.siteKino.mapper.FilmsMapper;
import com.kinoafisha.siteKino.model.CommentsModel;
import com.kinoafisha.siteKino.model.FilmModel;
import com.kinoafisha.siteKino.model.UsersModel;
import com.kinoafisha.siteKino.model.dto.CommentsShortDto;
import com.kinoafisha.siteKino.model.dto.FilmFullDto;
import com.kinoafisha.siteKino.model.dto.FilmsShortDto;
import com.kinoafisha.siteKino.repository.CommentsRepository;
import com.kinoafisha.siteKino.repository.FilmRepository;
import com.kinoafisha.siteKino.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmsService {

    private final FilmRepository filmRepository;

    private final UsersRepository usersRepository;

    private final CommentsRepository commentsRepository;

    private final FilmsMapper filmsMapper;
    private final CommentsMapper commentsMapper;

    public FilmFullDto findFilmById(Integer filmId){
        FilmModel filmModel = filmRepository.findFilmModelByFilmId(filmId);
        FilmFullDto filmDto = filmsMapper.toFilmFullDto(filmModel);
        return filmDto;
    }


    public FilmFullDto findFilmByNameDto(String name){
        FilmModel filmModel = filmRepository.findFilmModelByName(name);
        FilmFullDto filmDto = filmsMapper.toFilmFullDto(filmModel);
        return filmDto;
    }

    public FilmModel findFilmByNameModel(String name){
        FilmModel filmModel = filmRepository.findFilmModelByName(name);
        return filmModel;
    }


    public List<FilmsShortDto> getAllFilms(){
        List<FilmsShortDto> filmsShortDtos = new ArrayList<>();
        List<FilmModel> filmModels = filmRepository.findAll();
        for(FilmModel oneModel : filmModels)
        {
            FilmsShortDto shortDto = filmsMapper.toFilmsShortDto(oneModel);
            filmsShortDtos.add(shortDto);
        }
        return filmsShortDtos;
    }

    public CommentsModel addNewComment(String message)
    {
        CommentsShortDto commentsShortDto = new CommentsShortDto();
        UsersModel user = usersRepository.findUsersModelByAuthentificated(1);
        if(user!=null)
        {
            String login = user.getLogin();
            commentsShortDto.setMessage(message);
            commentsShortDto.setName(login);
            CommentsModel commentsModel = commentsMapper.toCommentsModel(commentsShortDto);
            commentsRepository.save(commentsModel);
            return commentsModel;

        }
        else{
            System.out.println("не авторизован");
            return null;
        }
        //Если чел авторизован - то есть нашли еденичку в базе данных, берем у пользователя с еденичкой имя
        // берем имедж

        //Из шорт Дто мапим в коммент
        //Сохраняем
    }


}
