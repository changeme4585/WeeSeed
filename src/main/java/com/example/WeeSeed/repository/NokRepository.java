package com.example.WeeSeed.repository;


import com.example.WeeSeed.entity.Nok;
import com.example.WeeSeed.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NokRepository extends JpaRepository<Nok, String> {
   //Optional<Nok> nok = findByNokId(String nokId);
}
