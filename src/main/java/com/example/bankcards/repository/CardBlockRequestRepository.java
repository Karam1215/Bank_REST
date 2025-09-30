package com.example.bankcards.repository;

import com.example.bankcards.entity.CardBlockRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, UUID> {

    Optional<CardBlockRequest> findByRequestId(Long requestId);
}
