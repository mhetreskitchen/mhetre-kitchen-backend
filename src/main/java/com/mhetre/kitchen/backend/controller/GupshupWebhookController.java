package com.mhetre.kitchen.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class GupshupWebhookController {

    private static final String GUPSHUP_API_URL = "https://api.gupshup.io/wa/api/v1/msg";
    private static final String GUPSHUP_API_KEY = "sk_b924fbe6ae544afb99d3ed65ad008377"; // replace with your key
    private static final String GUPSHUP_SOURCE = "917834811114"; // e.g., 918888888888

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        try {
            // Log full payload
            System.out.println("Received JSON: " + payload);

            // Navigate through nested payload
            Map<String, Object> outerPayload = (Map<String, Object>) payload.get("payload");
            Map<String, Object> innerPayload = (Map<String, Object>) outerPayload.get("payload");

            String messageText = (String) innerPayload.get("text");
            Map<String, Object> sender = (Map<String, Object>) innerPayload.get("sender");
            String userPhone = (String) sender.get("phone");

            System.out.println("User " + userPhone + " said: " + messageText);

            // Process user message and send a reply
            String reply = "Hi! You said: " + messageText;
            sendReply(userPhone, reply);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("ok");
    }

    private void sendReply(String toPhone, String messageText) {
        try {
            String json = String.format(
                    "{\"channel\":\"whatsapp\",\"source\":\"%s\",\"destination\":\"%s\",\"message\":{\"type\":\"text\",\"text\":\"%s\"}}",
                    GUPSHUP_SOURCE, toPhone, messageText.replace("\"", "\\\"")
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GUPSHUP_API_URL))
                    .header("Content-Type", "application/json")
                    .header("apikey", GUPSHUP_API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Reply sent. Gupshup response: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
