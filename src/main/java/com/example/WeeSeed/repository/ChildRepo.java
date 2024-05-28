package com.example.WeeSeed.repository;

import com.example.WeeSeed.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepo extends JpaRepository<Child, String> {
}

