package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.Pathologist;
import com.example.WeeSeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathologistRepository extends JpaRepository<Pathologist, String> {

}
