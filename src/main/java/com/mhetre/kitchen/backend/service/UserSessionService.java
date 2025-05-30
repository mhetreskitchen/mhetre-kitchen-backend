package com.mhetre.kitchen.backend.service;

import com.mhetre.kitchen.backend.model.UserSession;
import com.mhetre.kitchen.backend.model.UserState;
import com.mhetre.kitchen.backend.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {
    @Autowired
    private UserSessionRepository sessionRepository;

    public UserSession getOrCreateSession(String phone) {
        return sessionRepository.findByPhone(phone)
                .orElseGet(() -> {
                    UserSession session = new UserSession();
                    session.setPhone(phone);
                    session.setState(UserState.NEW);
                    session.setPaused(false);
                    return sessionRepository.save(session);
                });
    }

    public void saveSession(UserSession session) {
        sessionRepository.save(session);
    }
}
