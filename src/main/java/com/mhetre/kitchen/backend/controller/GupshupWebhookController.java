package com.mhetre.kitchen.backend.controller;

import com.mhetre.kitchen.backend.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
@RestController
public class GupshupWebhookController {

    @Autowired
    private WhatsAppService whatsAppService;

    // ✅ Real Gupshup POST webhook (used in production)
    @PostMapping(value = "/incoming", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> receiveMessagePost(@RequestParam Map<String, String> params) {
        System.out.println("=== Incoming Gupshup Webhook ===");
        params.forEach((key, value) -> System.out.println(key + " = " + value));

        String phone = params.get("source");
        String message = params.get("message");

        if (phone != null && message != null) {
            whatsAppService.handleIncomingMessage(phone, message);
            return ResponseEntity.ok("received");
        }

        return ResponseEntity.badRequest().body("Missing required params");
    }

    @GetMapping("/incoming")
    public ResponseEntity<String> receiveMessageGet(@RequestParam("source") String phone,
                                                    @RequestParam("message") String message) {
        String reply = whatsAppService.handleIncomingMessage(phone, message);
        return ResponseEntity.ok(reply);  // Return chatbot reply here
    }

}
