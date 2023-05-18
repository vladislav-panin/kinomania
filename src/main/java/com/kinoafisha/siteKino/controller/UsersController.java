package com.kinoafisha.siteKino.controller;

import com.kinoafisha.siteKino.AuthTrueEntity;
import com.kinoafisha.siteKino.mapper.FilmsMapper;
import com.kinoafisha.siteKino.mapper.RatingMapper;
import com.kinoafisha.siteKino.model.FilmModel;
import com.kinoafisha.siteKino.model.MyValue;
import com.kinoafisha.siteKino.model.RatingModel;
import com.kinoafisha.siteKino.model.UsersModel;
import com.kinoafisha.siteKino.model.dto.FilmFullDto;
import com.kinoafisha.siteKino.model.dto.FilmsShortDto;
import com.kinoafisha.siteKino.model.dto.RatingDto;
import com.kinoafisha.siteKino.model.dto.UserProfileDto;
import com.kinoafisha.siteKino.repository.AuthTrueRepository;
import com.kinoafisha.siteKino.repository.FilmRepository;
import com.kinoafisha.siteKino.repository.RatingRepository;
import com.kinoafisha.siteKino.repository.UsersRepository;
import com.kinoafisha.siteKino.service.FilmsService;
import com.kinoafisha.siteKino.service.RatingService;
import com.kinoafisha.siteKino.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("users")
@RequiredArgsConstructor
public class UsersController {


    private final UsersService usersService;

    private final UsersRepository usersRepository;

    private final RatingRepository ratingRepository;

    private final FilmsService filmsService;

    private final FilmRepository filmRepository;

    private final RatingService ratingService;

    private final FilmsMapper filmsMapper;

    private final RatingMapper ratingMapper;



    @GetMapping("/easy1")
    public String simpleReq(){
        return "easy";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new UsersModel());
        return "register_page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("loginRequest", new UsersModel());
        return "login_page";
    }
    //То что ниже сам написал
    @GetMapping("/logout")
    public String getLogoutPage(Model model){
        model.addAttribute("logoutRequest", new UsersModel());
        return "already_logged_in_error";
    }
    /*
    @GetMapping("/filmsAll")
    public String getFilmsAllPage(Model model){
        List<FilmsShortDto> filmsShortDtos = filmsService.getAllFilms();
        model.addAttribute("films", filmsShortDtos);
        return "all_films";
    }
*/

    @GetMapping("/profile")
    public String getProfilePage(Model model){
        UsersModel usersModel = usersRepository.findUsersModelByAuthentificated(1);
        Integer userId = usersModel.getUserId();
        UserProfileDto profile = usersService.getProfile(usersModel);
        List<RatingModel> ratingModelsWithHighRate = ratingRepository.findRatingModelsByRatingAndUserId(5,userId);//Кладу в список все рейтинги, у которых 5 (тут же лежат айди фильмов, которые имеют рейт 5)
        if(ratingModelsWithHighRate.size()!=0)
        {
            List<FilmsShortDto> fsd = new ArrayList<>();
            for(RatingModel ratingModel : ratingModelsWithHighRate)
            {
                ratingModel.getRatingId();
                Integer filmId = ratingModel.getFilmId();
                FilmModel filmModel = filmRepository.findFilmModelByFilmId(filmId);
                //Я хочу сразу сделать из этой модели короткое дто
                FilmsShortDto oneShortFilm = filmsMapper.toFilmsShortDto(filmModel);
                fsd.add(oneShortFilm);

            }

            model.addAttribute("films", fsd);
        }
        else {
            List<FilmsShortDto> filmsShortDtos = filmsService.getAllFilms();
            model.addAttribute("films", filmsShortDtos);
        }
        model.addAttribute("userLogin", usersModel.getLogin());
        model.addAttribute("profileRequest", profile);//Вот тут надо доделать следующую штуку - надо выводить сюда профильДто
        return "profile_page";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UsersModel usersModel)
    {
        System.out.println("register request: " + usersModel);
        UsersModel registeredUser =  usersService.registerUser(usersModel.getLogin(), usersModel.getPassword());
        registeredUser.setPreferences("Нет предпочтений");
        registeredUser.setBirthDate("Не задана");
        registeredUser.setAuthentificated(0);
        usersRepository.save(registeredUser);
        // return registeredUser == null ? "error_page" : "redirect/login"; //redirect/login
        if(registeredUser != null){
            return "login_page";
        }else{
            return "reg_error_page";
        }
    }




    @PostMapping("/login")
    public String login(@ModelAttribute UsersModel usersModel, Model model)
    {

            System.out.println("login request: " + usersModel);
            UsersModel authenticated = usersService.authenticate(usersModel.getLogin(), usersModel.getPassword());


            if (authenticated != null) {
                //////////////////////////////////
                if(authenticated.getAuthentificated() != 1) {
                    authenticated.setAuthentificated(1);
                 //   authenticated.setPreferences("нет предпочтений");
                //    authenticated.setBirthDate("не задана");

                    usersRepository.save(authenticated); // выше задаю значения для профиля, и обновляю сущность в таблице
                    model.addAttribute("userLogin", authenticated.getLogin());//Это добавление атрибута было по дефоту
                    List<FilmsShortDto> filmsShortDtos = filmsService.getAllFilms();// Просто собираю страницу со всеми фильмами
                    List<String> filmsNamesList = new ArrayList<>();
                    for(FilmsShortDto filmsShortDto : filmsShortDtos)
                    {
                        String filmName = filmsShortDto.getName();
                        filmsNamesList.add(filmName);
                    }
                    model.addAttribute("films", filmsShortDtos);
                    model.addAttribute("names",filmsNamesList);


                    //////////////////////////////////////////////////////////////////////////
                    return "main_page";
                }else {
                    return "already_logged_in_error";
                }
            } else {
                return "auth_error_page"; //
            }
    }



    @PostMapping("/logout") // если не заработает, надо бы заменить маппинг на login
    public String logout(@ModelAttribute UsersModel usersModel, Model model)
    {

        System.out.println("logout request: " + usersModel);
        UsersModel authenticated = usersRepository.findUsersModelByAuthentificated(1);


        if (authenticated != null) {
            //////////////////////////////////
            authenticated.setAuthentificated(0);

            usersRepository.save(authenticated); // выше задаю значения для профиля, и обновляю сущность в таблице
            ////////////////////////////////////
            model.addAttribute("userLogin", authenticated.getLogin());
            return "login_page";
        }else{
            return "easy";
        }
    }




    ////////////////////////////////////////////////////////////Отсюда идут фильмы

    @GetMapping("/films_all")//Рабочий эндпоинт
    public String getAll(Model model)// все фильмы
    {
        List<FilmsShortDto> filmsShortDtos = filmsService.getAllFilms();
        model.addAttribute("films", filmsShortDtos);
        return "main_page";
    }

/*
    @GetMapping("/films_all")
    public String getAll(@ModelAttribute Model model)// все фильмы
    {
        List<FilmsShortDto> filmsShortDtos = filmsService.getAllFilms();
        Integer size = filmsShortDtos.size();
        for(int i = 0; i<size; i++)
        {
           FilmsShortDto oneFilm = filmsShortDtos.get(i);
           Integer j = i;
           String jString = j.toString();
           String attributeName = "film" + jString;
           model.addAttribute(attributeName, oneFilm);
        }


        return "all_films";
    }
*/


    @GetMapping("one_film/{filmId}")
    public String getFilmByFilmId(@PathVariable Integer filmId, @ModelAttribute FilmModel filmModel, Model model){ // получение фильма по айди

        FilmFullDto filmFullDto = filmsService.findFilmById(filmId);

        return "film_page";

    }

    @GetMapping("/filmPage/{name}")
    public String getFilmByFilmName(@ModelAttribute FilmModel filmModel,  Model model){// получение фильма по имени
        String name = filmModel.getName();
        FilmFullDto filmFullDto = filmsService.findFilmByNameDto(name);

        FilmModel filmModelForId = filmRepository.findFilmModelByName(name);
        Integer filmId = filmModelForId.getFilmId();
        List<RatingModel> ratingModelList = ratingRepository.findRatingModelsByFilmId(filmId);
        Integer filmRating = 0;
        Integer filmRatingSum = 0;
        if(ratingModelList != null) {
            for (RatingModel ratingModel : ratingModelList) {
                filmRatingSum = filmRatingSum + ratingModel.getRating();

            }
            Integer ratingListSize = ratingModelList.size();
            filmRating = filmRatingSum/ratingListSize;
            filmFullDto.setRating(filmRating);

            FilmModel filmModelForBd = filmRepository.findFilmModelByName(name);
            filmModelForBd.setRating(filmRating);
            filmRepository.save(filmModelForBd);
        }else{
            filmFullDto.setRating(0);
        }

        model.addAttribute("film", filmFullDto); // перенес

        List<Integer> ratingScale = new ArrayList<>(); //Создаю свою шкалу рейтинга
        for(int i=0; i<6; i++)
        {
            ratingScale.add(i);//заполняю шкалу рейтинга
        }
        model.addAttribute("ratingScale", ratingScale);//добавляю атрибут шкалы рейтинга

        // model.addAttribute("film", filmFullDto);
        return "film_page";
    }
/*
    @GetMapping("/filmPage/{name}")
    public String getFilmByFilmName(@ModelAttribute FilmModel filmModel,  Model model){// получение фильма по имени
        String name = filmModel.getName();
        FilmFullDto filmFullDto = filmsService.findFilmByNameDto(name);
        model.addAttribute("film", filmFullDto); // перенес

        List<Integer> ratingScale = new ArrayList<>(); //Создаю свою шкалу рейтинга
        for(int i=0; i<6; i++)
        {
            ratingScale.add(i);//заполняю шкалу рейтинга
        }
        model.addAttribute("ratingScale", ratingScale);//добавляю атрибут шкалы рейтинга
        MyValue value = new MyValue();
        value.setValueInt(0);
        model.addAttribute("value", value);
        //НЕОБХОДИМО СФОРМИРОВАТЬ АТРИБУТ В HTML, И ПЕРЕДАТЬ ЕГО СЮДА. ЭТОТ АТРИБУТ БУДЕТ ХРАНИТЬ ЦИФРУ РЕЙТИНГА

        Integer myRating = number;// Я добавил в параметр рейтинг модель, не знаю, смог ли я здесь его получить или нет
        FilmModel filmModelForId = filmsService.findFilmByNameModel(name);
        Integer filmId = filmModelForId.getFilmId();
        UsersModel usersModel = usersRepository.findUsersModelByAuthentificated(1);
        Integer userId = usersModel.getUserId();
        RatingModel ratingModel = ratingRepository.findRatingModelByFilmIdAndUserId(filmId, userId);
        if(ratingModel != null){
            RatingDto ratingDto = ratingService.giveRatingToFilm(ratingModel, myRating); //Если рейтинг уже существует, то нужно пойти и изменить оценку
        }else{
            //Если юзер еще не ставил оценку данному фильму, то надо ПОЛУЧИТЬ ОТ ФРОНТА оценку, затем установить ее
            RatingModel newRatingModel = new RatingModel();
            newRatingModel.setFilmId(filmId);
            newRatingModel.setUserId(userId);
            newRatingModel.setRating(myRating);
            ratingRepository.save(newRatingModel);

        }
        Object tryRating = model.getAttribute("value");
        int k = 0;
        // model.addAttribute("film", filmFullDto);
        return "film_page";
    }
*/
    @GetMapping("/filmPage/0/{name}")
    public String getFilmByFilmNameR0(@ModelAttribute FilmModel filmModel,  Model model){// получение фильма по имени
        String name = filmModel.getName();
        FilmFullDto filmFullDto = filmsService.findFilmByNameDto(name);

        FilmModel filmModelForId1 = filmRepository.findFilmModelByName(name);
        Integer filmId1 = filmModelForId1.getFilmId();
        List<RatingModel> ratingModelList = ratingRepository.findRatingModelsByFilmId(filmId1);
        Integer filmRating = 0;
        Integer filmRatingSum = 0;
        if(ratingModelList != null) {
            for (RatingModel ratingModel : ratingModelList) {
                filmRatingSum = filmRatingSum + ratingModel.getRating();

            }
            Integer ratingListSize = ratingModelList.size();
            filmRating = filmRatingSum/ratingListSize;
            filmFullDto.setRating(filmRating);

            FilmModel filmModelForBd = filmRepository.findFilmModelByName(name);
            filmModelForBd.setRating(filmRating);
            filmRepository.save(filmModelForBd);
        }else{
            filmFullDto.setRating(0);
        }

        model.addAttribute("film", filmFullDto); // перенес

        List<Integer> ratingScale = new ArrayList<>(); //Создаю свою шкалу рейтинга
        for(int i=0; i<6; i++)
        {
            ratingScale.add(i);//заполняю шкалу рейтинга
        }
        model.addAttribute("ratingScale", ratingScale);//добавляю атрибут шкалы рейтинга

        Integer myRating = 0;// Я добавил в параметр рейтинг модель, не знаю, смог ли я здесь его получить или нет
        FilmModel filmModelForId = filmsService.findFilmByNameModel(name);
        Integer filmId = filmModelForId.getFilmId();
        UsersModel usersModel = usersRepository.findUsersModelByAuthentificated(1);
        Integer userId = usersModel.getUserId();
        RatingModel ratingModel = ratingRepository.findRatingModelByFilmIdAndUserId(filmId, userId);
        if(ratingModel != null){
            RatingDto ratingDto = ratingService.giveRatingToFilm(ratingModel, myRating); //Если рейтинг уже существует, то нужно пойти и изменить оценку
        }else{
            //Если юзер еще не ставил оценку данному фильму, то надо ПОЛУЧИТЬ ОТ ФРОНТА оценку, затем установить ее
            RatingModel newRatingModel = new RatingModel();
            newRatingModel.setFilmId(filmId);
            newRatingModel.setUserId(userId);
            newRatingModel.setRating(myRating);
            ratingRepository.save(newRatingModel);

        }

        // model.addAttribute("film", filmFullDto);
        return "film_page";
    }


    @GetMapping("/filmPage/1/{name}")
    public String getFilmByFilmNameR1(@ModelAttribute FilmModel filmModel,  Model model){// получение фильма по имени
        String name = filmModel.getName();
        FilmFullDto filmFullDto = filmsService.findFilmByNameDto(name);

        FilmModel filmModelForId1 = filmRepository.findFilmModelByName(name);
        Integer filmId1 = filmModelForId1.getFilmId();
        List<RatingModel> ratingModelList = ratingRepository.findRatingModelsByFilmId(filmId1);
        Integer filmRating = 0;
        Integer filmRatingSum = 0;
        if(ratingModelList != null) {
            for (RatingModel ratingModel : ratingModelList) {
                filmRatingSum = filmRatingSum + ratingModel.getRating();

            }
            Integer ratingListSize = ratingModelList.size();
            filmRating = filmRatingSum/ratingListSize;
            filmFullDto.setRating(filmRating);

            FilmModel filmModelForBd = filmRepository.findFilmModelByName(name);
            filmModelForBd.setRating(filmRating);
            filmRepository.save(filmModelForBd);
        }else{
            filmFullDto.setRating(0);
        }


        model.addAttribute("film", filmFullDto); // перенес

        List<Integer> ratingScale = new ArrayList<>(); //Создаю свою шкалу рейтинга
        for(int i=0; i<6; i++)
        {
            ratingScale.add(i);//заполняю шкалу рейтинга
        }
        model.addAttribute("ratingScale", ratingScale);//добавляю атрибут шкалы рейтинга

        Integer myRating = 1;// Я добавил в параметр рейтинг модель, не знаю, смог ли я здесь его получить или нет
        FilmModel filmModelForId = filmsService.findFilmByNameModel(name);
        Integer filmId = filmModelForId.getFilmId();
        UsersModel usersModel = usersRepository.findUsersModelByAuthentificated(1);
        Integer userId = usersModel.getUserId();
        RatingModel ratingModel = ratingRepository.findRatingModelByFilmIdAndUserId(filmId, userId);
        if(ratingModel != null){
            RatingDto ratingDto = ratingService.giveRatingToFilm(ratingModel, myRating); //Если рейтинг уже существует, то нужно пойти и изменить оценку
        }else{
            //Если юзер еще не ставил оценку данному фильму, то надо ПОЛУЧИТЬ ОТ ФРОНТА оценку, затем установить ее
            RatingModel newRatingModel = new RatingModel();
            newRatingModel.setFilmId(filmId);
            newRatingModel.setUserId(userId);
            newRatingModel.setRating(myRating);
            ratingRepository.save(newRatingModel);

        }

        // model.addAttribute("film", filmFullDto);
        return "film_page";
    }

    @GetMapping("/filmPage/2/{name}")
    public String getFilmByFilmNameR2(@ModelAttribute FilmModel filmModel,  Model model){// получение фильма по имени
        String name = filmModel.getName();
        FilmFullDto filmFullDto = filmsService.findFilmByNameDto(name);

        FilmModel filmModelForId1 = filmRepository.findFilmModelByName(name);
        Integer filmId1 = filmModelForId1.getFilmId();
        List<RatingModel> ratingModelList = ratingRepository.findRatingModelsByFilmId(filmId1);
        Integer filmRating = 0;
        Integer filmRatingSum = 0;
        if(ratingModelList != null) {
            for (RatingModel ratingModel : ratingModelList) {
                filmRatingSum = filmRatingSum + ratingModel.getRating();

            }
            Integer ratingListSize = ratingModelList.size();
            filmRating = filmRatingSum/ratingListSize;
            filmFullDto.setRating(filmRating);

            FilmModel filmModelForBd = filmRepository.findFilmModelByName(name);
            filmModelForBd.setRating(filmRating);
            filmRepository.save(filmModelForBd);
        }else{
            filmFullDto.setRating(0);
        }

        model.addAttribute("film", filmFullDto); // перенес

        List<Integer> ratingScale = new ArrayList<>(); //Создаю свою шкалу рейтинга
        for(int i=0; i<6; i++)
        {
            ratingScale.add(i);//заполняю шкалу рейтинга
        }
        model.addAttribute("ratingScale", ratingScale);//добавляю атрибут шкалы рейтинга

        Integer myRating = 2;// Я добавил в параметр рейтинг модель, не знаю, смог ли я здесь его получить или нет
        FilmModel filmModelForId = filmsService.findFilmByNameModel(name);
        Integer filmId = filmModelForId.getFilmId();
        UsersModel usersModel = usersRepository.findUsersModelByAuthentificated(1);
        Integer userId = usersModel.getUserId();
        RatingModel ratingModel = ratingRepository.findRatingModelByFilmIdAndUserId(filmId, userId);
        if(ratingModel != null){
            RatingDto ratingDto = ratingService.giveRatingToFilm(ratingModel, myRating); //Если рейтинг уже существует, то нужно пойти и изменить оценку
        }else{
            //Если юзер еще не ставил оценку данному фильму, то надо ПОЛУЧИТЬ ОТ ФРОНТА оценку, затем установить ее
            RatingModel newRatingModel = new RatingModel();
            newRatingModel.setFilmId(filmId);
            newRatingModel.setUserId(userId);
            newRatingModel.setRating(myRating);
            ratingRepository.save(newRatingModel);

        }

        // model.addAttribute("film", filmFullDto);
        return "film_page";
    }
    @GetMapping("/filmPage/3/{name}")
    public String getFilmByFilmNameR3(@ModelAttribute FilmModel filmModel,  Model model){// получение фильма по имени
        String name = filmModel.getName();
        FilmFullDto filmFullDto = filmsService.findFilmByNameDto(name);

        FilmModel filmModelForId1 = filmRepository.findFilmModelByName(name);
        Integer filmId1 = filmModelForId1.getFilmId();
        List<RatingModel> ratingModelList = ratingRepository.findRatingModelsByFilmId(filmId1);
        Integer filmRating = 0;
        Integer filmRatingSum = 0;
        if(ratingModelList != null) {
            for (RatingModel ratingModel : ratingModelList) {
                filmRatingSum = filmRatingSum + ratingModel.getRating();

            }
            Integer ratingListSize = ratingModelList.size();
            filmRating = filmRatingSum/ratingListSize;
            filmFullDto.setRating(filmRating);

            FilmModel filmModelForBd = filmRepository.findFilmModelByName(name);
            filmModelForBd.setRating(filmRating);
            filmRepository.save(filmModelForBd);
        }else{
            filmFullDto.setRating(0);
        }

        model.addAttribute("film", filmFullDto); // перенес

        List<Integer> ratingScale = new ArrayList<>(); //Создаю свою шкалу рейтинга
        for(int i=0; i<6; i++)
        {
            ratingScale.add(i);//заполняю шкалу рейтинга
        }
        model.addAttribute("ratingScale", ratingScale);//добавляю атрибут шкалы рейтинга

        Integer myRating = 3;// Я добавил в параметр рейтинг модель, не знаю, смог ли я здесь его получить или нет
        FilmModel filmModelForId = filmsService.findFilmByNameModel(name);
        Integer filmId = filmModelForId.getFilmId();
        UsersModel usersModel = usersRepository.findUsersModelByAuthentificated(1);
        Integer userId = usersModel.getUserId();
        RatingModel ratingModel = ratingRepository.findRatingModelByFilmIdAndUserId(filmId, userId);
        if(ratingModel != null){
            RatingDto ratingDto = ratingService.giveRatingToFilm(ratingModel, myRating); //Если рейтинг уже существует, то нужно пойти и изменить оценку
        }else{
            //Если юзер еще не ставил оценку данному фильму, то надо ПОЛУЧИТЬ ОТ ФРОНТА оценку, затем установить ее
            RatingModel newRatingModel = new RatingModel();
            newRatingModel.setFilmId(filmId);
            newRatingModel.setUserId(userId);
            newRatingModel.setRating(myRating);
            ratingRepository.save(newRatingModel);

        }

        // model.addAttribute("film", filmFullDto);
        return "film_page";
    }
    @GetMapping("/filmPage/4/{name}")
    public String getFilmByFilmNameR4(@ModelAttribute FilmModel filmModel,  Model model){// получение фильма по имени
        String name = filmModel.getName();
        FilmFullDto filmFullDto = filmsService.findFilmByNameDto(name);

        FilmModel filmModelForId1 = filmRepository.findFilmModelByName(name);
        Integer filmId1 = filmModelForId1.getFilmId();
        List<RatingModel> ratingModelList = ratingRepository.findRatingModelsByFilmId(filmId1);
        Integer filmRating = 0;
        Integer filmRatingSum = 0;
        if(ratingModelList != null) {
            for (RatingModel ratingModel : ratingModelList) {
                filmRatingSum = filmRatingSum + ratingModel.getRating();

            }
            Integer ratingListSize = ratingModelList.size();
            filmRating = filmRatingSum/ratingListSize;
            filmFullDto.setRating(filmRating);

            FilmModel filmModelForBd = filmRepository.findFilmModelByName(name);
            filmModelForBd.setRating(filmRating);
            filmRepository.save(filmModelForBd);

        }else{
            filmFullDto.setRating(0);
        }

        model.addAttribute("film", filmFullDto); // перенес

        List<Integer> ratingScale = new ArrayList<>(); //Создаю свою шкалу рейтинга
        for(int i=0; i<6; i++)
        {
            ratingScale.add(i);//заполняю шкалу рейтинга
        }
        model.addAttribute("ratingScale", ratingScale);//добавляю атрибут шкалы рейтинга

        Integer myRating = 4;// Я добавил в параметр рейтинг модель, не знаю, смог ли я здесь его получить или нет
        FilmModel filmModelForId = filmsService.findFilmByNameModel(name);
        Integer filmId = filmModelForId.getFilmId();
        UsersModel usersModel = usersRepository.findUsersModelByAuthentificated(1);
        Integer userId = usersModel.getUserId();
        RatingModel ratingModel = ratingRepository.findRatingModelByFilmIdAndUserId(filmId, userId);
        if(ratingModel != null){
            RatingDto ratingDto = ratingService.giveRatingToFilm(ratingModel, myRating); //Если рейтинг уже существует, то нужно пойти и изменить оценку
        }else{
            //Если юзер еще не ставил оценку данному фильму, то надо ПОЛУЧИТЬ ОТ ФРОНТА оценку, затем установить ее
            RatingModel newRatingModel = new RatingModel();
            newRatingModel.setFilmId(filmId);
            newRatingModel.setUserId(userId);
            newRatingModel.setRating(myRating);
            ratingRepository.save(newRatingModel);

        }

        // model.addAttribute("film", filmFullDto);
        return "film_page";
    }
    @GetMapping("/filmPage/5/{name}")
    public String getFilmByFilmNameR5(@ModelAttribute FilmModel filmModel,  Model model){// получение фильма по имени
        String name = filmModel.getName();
        FilmFullDto filmFullDto = filmsService.findFilmByNameDto(name);

        FilmModel filmModelForId1 = filmRepository.findFilmModelByName(name);
        Integer filmId1 = filmModelForId1.getFilmId();
        List<RatingModel> ratingModelList = ratingRepository.findRatingModelsByFilmId(filmId1);
        Integer filmRating = 0;
        Integer filmRatingSum = 0;
        if(ratingModelList != null) {
            for (RatingModel ratingModel : ratingModelList) {
                filmRatingSum = filmRatingSum + ratingModel.getRating();

            }
            Integer ratingListSize = ratingModelList.size();
            filmRating = filmRatingSum/ratingListSize;
            filmFullDto.setRating(filmRating);

            FilmModel filmModelForBd = filmRepository.findFilmModelByName(name);
            filmModelForBd.setRating(filmRating);
            filmRepository.save(filmModelForBd);

        }else{
            filmFullDto.setRating(0);
        }

        model.addAttribute("film", filmFullDto); // перенес

        List<Integer> ratingScale = new ArrayList<>(); //Создаю свою шкалу рейтинга
        for(int i=0; i<6; i++)
        {
            ratingScale.add(i);//заполняю шкалу рейтинга
        }
        model.addAttribute("ratingScale", ratingScale);//добавляю атрибут шкалы рейтинга

        Integer myRating = 5;// Я добавил в параметр рейтинг модель, не знаю, смог ли я здесь его получить или нет
        FilmModel filmModelForId = filmsService.findFilmByNameModel(name);
        Integer filmId = filmModelForId.getFilmId();
        UsersModel usersModel = usersRepository.findUsersModelByAuthentificated(1);
        Integer userId = usersModel.getUserId();
        RatingModel ratingModel = ratingRepository.findRatingModelByFilmIdAndUserId(filmId, userId);
        if(ratingModel != null){
            RatingDto ratingDto = ratingService.giveRatingToFilm(ratingModel, myRating); //Если рейтинг уже существует, то нужно пойти и изменить оценку
        }else{
            //Если юзер еще не ставил оценку данному фильму, то надо ПОЛУЧИТЬ ОТ ФРОНТА оценку, затем установить ее
            RatingModel newRatingModel = new RatingModel();
            newRatingModel.setFilmId(filmId);
            newRatingModel.setUserId(userId);
            newRatingModel.setRating(myRating);
            ratingRepository.save(newRatingModel);

        }

        // model.addAttribute("film", filmFullDto);
        return "film_page";
    }



}
