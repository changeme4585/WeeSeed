package org.weeseed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weeseed.entity.ExtendedAacCard;

import java.util.List;

public interface ExtendedAacCardRepository extends JpaRepository<ExtendedAacCard, Long> {
    List<ExtendedAacCard> findByRepCardId(Long repCardId);
}