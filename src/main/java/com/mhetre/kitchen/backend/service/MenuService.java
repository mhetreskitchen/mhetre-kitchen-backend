package com.mhetre.kitchen.backend.service;

import com.mhetre.kitchen.backend.model.Menu;
import com.mhetre.kitchen.backend.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import java.util.Optional;
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    public String getTodayMenuString() {
        Menu menu = getTodayMenu();
        if (menu == null) {
            return "Sorry, today's menu is not available.";
        }
        return "🍽️ Today's Menu:\n"
                + "1️⃣ " + menu.getItem1() + "\n"
                + "2️⃣ " + menu.getItem2() + "\n"
                + "🍰 " + menu.getDessert();
    }
    // Method to get today's menu
    public Menu getTodayMenu() {
        LocalDate today = LocalDate.now();
        Optional<Menu> optionalMenu = menuRepository.findByDate(today);
        return optionalMenu.orElse(null);  // or throw exception if you want
    }
}
