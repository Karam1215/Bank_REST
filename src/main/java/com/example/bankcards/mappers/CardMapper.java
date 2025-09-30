package com.example.bankcards.mappers;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.dto.UserShortDTOForCard;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Mapping(target = "user", expression = "java(mapUser(card.getUser()))")
    @Mapping(target = "cardNumber", expression = "java(card.getMaskedCardNumber())")
    CardResponseDTO toResponseDto(Card card);

    List<CardResponseDTO> toResponseDtoList(List<Card> cards);

    default UserShortDTOForCard mapUser(User user) {
        if (user == null) {
            return null;
        }
        return new UserShortDTOForCard(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBirthDate()
        );
    }
}
