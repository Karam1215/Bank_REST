package com.example.bankcards.mappers;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "maskedCardNumber", expression = "java(card.getMaskedCardNumber())")
    @Mapping(target = "createdBy", source = "createdBy.userId")
    CardResponseDTO toResponseDto(Card card);
}
