package com.mhetre.kitchen.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class GupshupWebhookController {

    // Handle POST (real messages)
    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody String payload) {
        System.out.println("Received POST from Gupshup: " + payload);
        return ResponseEntity.ok("OK");
    }

    // Optional: Handle GET (Gupshup verification or browser check)
    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook() {
        return ResponseEntity.ok("Webhook is live");
    }
}
