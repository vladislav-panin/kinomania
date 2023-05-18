package com.kinoafisha.siteKino.service;

import com.kinoafisha.siteKino.AuthTrueEntity;
import com.kinoafisha.siteKino.mapper.UsersMapper;
import com.kinoafisha.siteKino.model.UsersModel;
import com.kinoafisha.siteKino.model.dto.UserProfileDto;
import com.kinoafisha.siteKino.repository.AuthTrueRepository;
import com.kinoafisha.siteKino.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {


    private final UsersRepository usersRepository;


    private final UsersMapper usersMapper;

    public UsersModel registerUser(String login, String password)
    {
        UsersModel usersModelInBd = usersRepository.findByLogin(login);
        if(usersModelInBd != null)
        {
            String loginInBd = usersModelInBd.getLogin();
            System.out.println(loginInBd);
            return null;
        } else
        {
            UsersModel usersModel = new UsersModel();
            usersModel.setLogin(login);
            usersModel.setPassword(password);
            return usersRepository.save(usersModel);
        }
    }


    public UserProfileDto getProfile(UsersModel usersModel){
        return usersMapper.toUserProfileDto(usersModel);
    }

    public UsersModel authenticate(String login, String password){



        UsersModel user = usersRepository.findByLoginAndPassword(login,password);

        if(user != null)
        {



             return user;

        }
        else {
            return null;
        }
    }
}
