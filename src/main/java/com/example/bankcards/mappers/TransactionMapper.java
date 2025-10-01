package com.example.bankcards.mappers;

import com.example.bankcards.dto.TransactionReturnDTO;
import com.example.bankcards.dto.UserShortDTOForCard;
import com.example.bankcards.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "originCardNumber", expression = "java(transaction.getOriginCard().getMaskedCardNumber())")
    @Mapping(target = "destinationCardNumber", expression = "java(transaction.getDestinationCard().getMaskedCardNumber())")
    @Mapping(target = "user", expression = "java(toUserShortDTO(transaction.getOriginCard().getUser()))")
    TransactionReturnDTO toAdminDTO(Transaction transaction);

    default UserShortDTOForCard toUserShortDTO(com.example.bankcards.entity.User user) {
        if (user == null) return null;
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
