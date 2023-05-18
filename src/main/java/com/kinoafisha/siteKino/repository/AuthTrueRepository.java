package com.kinoafisha.siteKino.repository;

import com.kinoafisha.siteKino.AuthTrueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTrueRepository extends JpaRepository<AuthTrueEntity, Integer> {

    AuthTrueEntity findAuthTrueEntityByValue(Integer value);
}
