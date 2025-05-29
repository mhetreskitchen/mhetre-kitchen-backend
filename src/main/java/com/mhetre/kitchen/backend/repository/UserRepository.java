package com.mhetre.kitchen.backend.repository;

import com.mhetre.kitchen.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
