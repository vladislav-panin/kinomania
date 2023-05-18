package com.kinoafisha.siteKino.repository;

import com.kinoafisha.siteKino.model.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersModel, Integer> {
    UsersModel findByLoginAndPassword(String login, String password);
    UsersModel findByLogin(String login);


    UsersModel findUsersModelByAuthentificated(Integer authentificated);
}
