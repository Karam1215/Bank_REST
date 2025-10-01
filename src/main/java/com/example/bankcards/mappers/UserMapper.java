package com.example.bankcards.mappers;

import com.example.bankcards.dto.UpdateUserProfileDTO;
import com.example.bankcards.dto.UserProfileDTO;
import com.example.bankcards.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Обновляет существующего пользователя на основе данных из DTO.
     * Поля со значением null в DTO будут проигнорированы.
     *
     * @param dto данные для обновления профиля
     * @param user сущность пользователя, которую нужно обновить
     */
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "middleName", source = "middleName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gender", source = "gender")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserProfileDTO dto, @MappingTarget User user);


    /**
     * Преобразует сущность User в DTO UserDTO.
     */
    UserProfileDTO toDto(User user);

    /**
     * Преобразует список сущностей User в список DTO UserDTO.
     */
    List<UserProfileDTO> toDtoList(List<User> users);
}
