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

    public String handleIncomingMessage(String phone, String message) {
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

        // Optionally, you can still send the message asynchronously via WhatsApp API if needed
        // sendMessage(phone, reply);

        return reply;  // Return reply string instead of void
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
        String url = "https://api.gupshup.io/wa/api/v1/msg"; // ✅ fixed endpoint

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", "gmiv8duabpe67w2ly30a1mmx7ltfbghn"); // ✅ your correct API key
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("channel", "whatsapp");
        params.add("source", "917834811114");
        params.add("destination", phone);

        String jsonMessage = "{\"type\":\"text\",\"text\":\"" + message + "\"}";
        params.add("message", jsonMessage); // ✅ correct format

        params.add("src.name", "Mhetreskitchen");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        restTemplate.postForEntity(url, request, String.class);
    }

}