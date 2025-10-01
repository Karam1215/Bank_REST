package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionReturnDTO;
import com.example.bankcards.dto.TransferRequestDTO;
import com.example.bankcards.dto.TransferResponseDTO;
import com.example.bankcards.dto.UserShortDTOForCard;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.mappers.TransactionMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferService {

    private final CardRepository cardRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    public ResponseEntity<TransferResponseDTO> transfer(TransferRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getName());

        Card origin = cardRepository.findById(dto.originCardId())
                .orElseThrow(() -> new CardNotFoundException("Исходная карта не найдена"));

        Card destination = cardRepository.findById(dto.destinationCardId())
                .orElseThrow(() -> new CardNotFoundException("Карта получателя не найдена"));

        if (!origin.getUser().getUserId().equals(userId) || !destination.getUser().getUserId().equals(userId)) {
            throw new SecurityException("Карта не принадлежит пользователю");
        }

        if (origin.getBalance().compareTo(dto.amount()) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на карте");
        }

        origin.setBalance(origin.getBalance().subtract(dto.amount()));
        destination.setBalance(destination.getBalance().add(dto.amount()));

        Transaction tx = Transaction.builder()
                .originCard(origin)
                .destinationCard(destination)
                .amount(dto.amount())
                .build();

        transactionRepository.save(tx);

        TransferResponseDTO response = new TransferResponseDTO(
                tx.getTransactionId(),
                origin.getCardId(),
                destination.getCardId(),
                dto.amount(),
                tx.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Page<TransactionReturnDTO>> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        Page<TransactionReturnDTO> dtoPage = transactions.map(transactionMapper::toAdminDTO);
        return ResponseEntity.ok(dtoPage);
    }

public ResponseEntity<Page<TransactionReturnDTO>> getTransactionsByUser(Pageable pageable) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UUID userId = UUID.fromString(authentication.getName());

    Page<Transaction> transactions = transactionRepository.findByOriginCard_User_UserId(userId, pageable);

    Page<TransactionReturnDTO> dtoPage = transactions.map(t -> {
        UserShortDTOForCard userDto = new UserShortDTOForCard(
                t.getOriginCard().getUser().getUserId(),
                t.getOriginCard().getUser().getFirstName(),
                t.getOriginCard().getUser().getLastName(),
                t.getOriginCard().getUser().getEmail(),
                t.getOriginCard().getUser().getPhoneNumber(),
                t.getOriginCard().getUser().getBirthDate()
        );

        return new TransactionReturnDTO(
                t.getTransactionId(),
                t.getAmount(),
                t.getCreatedAt(),
                t.getOriginCard().getMaskedCardNumber(),
                t.getDestinationCard().getMaskedCardNumber(),
                userDto
        );
    });

    return ResponseEntity.ok(dtoPage);
}


}
