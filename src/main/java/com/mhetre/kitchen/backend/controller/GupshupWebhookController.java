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
    private static final String GUPSHUP_SOURCE = "917834811114"; // your Gupshup sandbox or number
    private static final String TEMPLATE_NAME = "welcome_template_name"; // replace with your template's exact name from Gupshup dashboard

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        try {
            // Log full payload
            System.out.println("Received JSON: " + payload);

            Map<String, Object> outerPayload = (Map<String, Object>) payload.get("payload");
            Map<String, Object> innerPayload = (Map<String, Object>) outerPayload.get("payload");

            String messageText = (String) innerPayload.get("text");
            Map<String, Object> sender = (Map<String, Object>) innerPayload.get("sender");
            String userPhone = (String) sender.get("phone");

            System.out.println("User " + userPhone + " said: " + messageText);

            // For demo, we will fill the template param with user phone number (or you can extract name if available)
            String userName = userPhone; // Or parse user name if you have it

            sendTemplateMessage(userPhone, userName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("ok");
    }

    private void sendTemplateMessage(String toPhone, String param1) {
        try {
            String json = String.format(
                    """
                    {
                      "channel": "whatsapp",
                      "source": "%s",
                      "destination": "%s",
                      "message": {
                        "type": "template",
                        "template": {
                          "name": "%s",
                          "params": ["%s"]
                        }
                      }
                    }
                    """,
                    GUPSHUP_SOURCE, toPhone, TEMPLATE_NAME, param1.replace("\"", "\\\"")
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GUPSHUP_API_URL))
                    .header("Content-Type", "application/json")
                    .header("apikey", GUPSHUP_API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Template message sent. Gupshup response: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
