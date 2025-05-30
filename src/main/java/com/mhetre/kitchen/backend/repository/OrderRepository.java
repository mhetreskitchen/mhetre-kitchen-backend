package com.mhetre.kitchen.backend.repository;

import com.mhetre.kitchen.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface OrderRepository extends JpaRepository<Order, Long> {
        boolean existsByPhoneAndDate(String phone, LocalDate date);
    }

