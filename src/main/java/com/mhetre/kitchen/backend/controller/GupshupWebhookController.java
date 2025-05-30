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
            System.out.println("Received JSON: " + payload);

            // Check if "entry" is present and is a list
            var entryList = (java.util.List<Map<String, Object>>) payload.get("entry");
            if (entryList == null || entryList.isEmpty()) {
                System.out.println("No entry found in payload");
                return ResponseEntity.ok("no entry");
            }

            // Iterate over entries (usually only one)
            for (Map<String, Object> entry : entryList) {
                var changesList = (java.util.List<Map<String, Object>>) entry.get("changes");
                if (changesList == null || changesList.isEmpty()) {
                    System.out.println("No changes found in entry");
                    continue;
                }

                for (Map<String, Object> change : changesList) {
                    String field = (String) change.get("field");
                    Map<String, Object> value = (Map<String, Object>) change.get("value");
                    if ("messages".equals(field) && value != null) {
                        // Check if the payload has 'messages' or 'statuses'
                        if (value.containsKey("messages")) {
                            var messages = (java.util.List<Map<String, Object>>) value.get("messages");
                            if (messages != null && !messages.isEmpty()) {
                                Map<String, Object> message = messages.get(0);
                                String messageText = null;
                                if ("text".equals(message.get("type"))) {
                                    Map<String, Object> textObj = (Map<String, Object>) message.get("text");
                                    messageText = (String) textObj.get("body");
                                }
                                Map<String, Object> sender = (Map<String, Object>) message.get("from"); // or other sender key
                                String userPhone = (String) message.get("from"); // or adapt to correct key
                                System.out.println("User " + userPhone + " said: " + messageText);

                                // Process user message and send a reply
                                if (messageText != null) {
                                    String reply = "Hi! You said: " + messageText;
                                    sendTemplateMessage(userPhone, reply);
                                }
                            }
                        } else if (value.containsKey("statuses")) {
                            var statuses = (java.util.List<Map<String, Object>>) value.get("statuses");
                            if (statuses != null && !statuses.isEmpty()) {
                                Map<String, Object> status = statuses.get(0);
                                String statusString = (String) status.get("status");
                                System.out.println("Received status update: " + statusString);
                                // handle statuses if needed
                            }
                        }
                    }
                }
            }
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
