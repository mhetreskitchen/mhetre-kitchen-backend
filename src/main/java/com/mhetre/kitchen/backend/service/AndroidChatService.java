package com.mhetre.kitchen.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AndroidChatService {

    @Autowired
    private UserSessionService sessionService;

    public String handleMessage(String phone, String message) {
        message = message.trim().toLowerCase();

        if (message.equals("hi") || message.equals("hello")) {
            return handleWelcomeMessage();
        }

        // Add logic for different options
        switch (message) {
            case "1":
                return "📋 Today's Menu:\n🍚 Rice\n🍛 Dal\n🥗 Salad";
            case "2":
                return "🔕 Your subscription has been paused.";
            case "order":
                return "📦 Your order for today has been placed!";
            case "subscription":
                return "📋 Subscription: Active\nPlan: Lunch Only\nDays: Mon-Fri";
            case "my info":
                return "👤 Name: John Doe\nPhone: " + phone;
            case "help":
                return handleWelcomeMessage();
            default:
                return "❓ Sorry, I didn't understand that.\nType *help* to see all options.";
        }
    }

    private String handleWelcomeMessage() {
        return "👋 Welcome to *Mhetre's Kitchen*!\n" +
                "Reply with:\n" +
                "1️⃣ - Today's Menu\n" +
                "2️⃣ - Pause Subscription\n" +
                "💡 Type *help* for all options\n" +
                "📦 Type *order* to place your meal\n" +
                "📋 Type *subscription* for your subscription details\n" +
                "👤 Type *my info* to view your profile";
    }
}
