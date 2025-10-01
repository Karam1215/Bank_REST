package com.example.bankcards.mappers;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CardBlockRequestMapper {

    CardBlockRequestMapper INSTANCE = Mappers.getMapper(CardBlockRequestMapper.class);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "card", source = "card")
    @Mapping(target = "processedBy", source = "processedBy")
    CardBlockRequestDTO toDTO(CardBlockRequest request);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "cardNumber", expression = "java(card.getMaskedCardNumber())")
    CardResponseDTO toDTO(Card card);

    UserShortDTOForCard toShortDTO(User user);
}
