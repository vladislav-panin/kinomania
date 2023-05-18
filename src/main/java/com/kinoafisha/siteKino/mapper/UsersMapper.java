package com.kinoafisha.siteKino.mapper;

import com.kinoafisha.siteKino.model.UsersModel;
import com.kinoafisha.siteKino.model.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersMapper {

    //ПОКА МАПЛЮ БЕЗ СПИСКА ПОНРАВИВШИХСЯ ФИЛЬМОВ, МБ ВООБЩЕ НЕ ПОНАДОБИТСЯ ЕГО ДЕЛАТЬ, ЕСЛИ ПОНРАВИВШИЕСЯ ФИЛЬМЫ БУДУТ ОТДЕЛЬНОЙ ТАБЛИЦЕЙ
    private final ModelMapper modelMapper;

    public UserProfileDto toUserProfileDto(UsersModel usersModel)
    {
        return modelMapper.map(usersModel,UserProfileDto.class);
    }


}
