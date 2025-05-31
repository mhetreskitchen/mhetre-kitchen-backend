package com.mhetre.kitchen.backend.controller;

import com.mhetre.kitchen.backend.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.badRequest().body("❌ Missing required parameters: source and message");
    }

    // ✅ GET endpoint for browser/manual testing
    @GetMapping("/incoming")
    public ResponseEntity<String> receiveMessageGet(
            @RequestParam(value = "source", defaultValue = "test-user") String phone,
            @RequestParam(value = "message", defaultValue = "hi") String message) {

        String reply = whatsAppService.handleIncomingMessage(phone, message);
        return ResponseEntity.ok(reply);  // Return chatbot reply
    }
}
