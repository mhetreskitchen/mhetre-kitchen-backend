package com.mhetre.kitchen.backend.service;
import com.mhetre.kitchen.backend.model.UserState;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mhetre.kitchen.backend.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    @Autowired
    private UserSessionService sessionService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private OrderService orderService;

    public void handleIncomingMessage(String phone, String message) {
        UserSession session = sessionService.getOrCreateSession(phone);
        String reply;

        switch (session.getState()) {
            case NEW:
                reply = "\uD83D\uDC4B Welcome! What's your name?";
                session.setState(UserState.WAITING_FOR_NAME);
                break;

            case WAITING_FOR_NAME:
                session.setName(message);
                session.setState(UserState.WAITING_FOR_PLAN);
                reply = "Nice to meet you, *" + message + "*!\nPlease type your meal plan (Basic/Pro):";
                break;

            case WAITING_FOR_PLAN:
                session.setPlan(message);
                session.setState(UserState.ACTIVE);
                reply = "\u2705 You're all set!\n\n" + mainMenu();
                break;

            case ACTIVE:
                reply = processMainCommands(message, session);
                break;

            default:
                reply = "⚠️ Unknown state. Please type *help*.";
        }

        sessionService.saveSession(session);
        sendMessage(phone, reply);
    }

    private String processMainCommands(String msg, UserSession session) {
        msg = msg.toLowerCase();

        if (msg.equals("1") || msg.contains("menu")) {
            return menuService.getTodayMenuString();
                } else if (msg.contains("pause")) {
            session.setPaused(true);
            return "🔕 Subscription paused. Type *resume* to activate again.";
        } else if (msg.contains("resume")) {
            session.setPaused(false);
            return "✅ Subscription resumed!";
        } else if (msg.contains("order")) {
            return orderService.processOrder(session);
        } else if (msg.contains("subscription")) {
            return "\uD83D\uDCE6 Plan: *" + session.getPlan() + "*\nStatus: " + (session.isPaused() ? "Paused" : "Active");
        } else if (msg.contains("my info")) {
            return "\uD83D\uDC64 Name: " + session.getName() + "\n\uD83D\uDCE6 Plan: " + session.getPlan();
        } else if (msg.contains("help")) {
            return mainMenu();
        } else {
            return "❓ Sorry, I didn't understand. Type *help* for options.";
        }
    }

    private String mainMenu() {
        return """
1️⃣ - Today's Menu
2️⃣ - Pause Subscription
💡 Type *help* for all options
📦 Type *order* to place your meal
📋 Type *subscription* for your subscription details
👤 Type *my info* to view your profile
""";
    }

    private void sendMessage(String phone, String message) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.gupshup.io/sm/api/v1/msg";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", "YOUR_GUPSHUP_API_KEY");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("channel", "whatsapp");
        params.add("source", "YOUR_GUPSHUP_NUMBER");
        params.add("destination", phone);
        params.add("message", message);
        params.add("src.name", "YOUR_GUPSHUP_APP_NAME");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        restTemplate.postForEntity(url, request, String.class);
    }
}