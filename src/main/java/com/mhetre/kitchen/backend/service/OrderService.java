package com.mhetre.kitchen.backend.service;

import com.mhetre.kitchen.backend.model.Order;
import com.mhetre.kitchen.backend.model.UserSession;
import com.mhetre.kitchen.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public String processOrder(UserSession session) {
        LocalDate today = LocalDate.now();

        if (session.isPaused()) {
            return "🚫 Subscription paused. Type *resume* to order again.";
        }

        if (orderRepository.existsByPhoneAndDate(session.getPhone(), today)) {
            return "✅ You’ve already ordered today!";
        }

        Order order = new Order();
        order.setPhone(session.getPhone());
        order.setDate(today);
        orderRepository.save(order);

        return "✅ Your meal has been ordered for today!";
    }
}
