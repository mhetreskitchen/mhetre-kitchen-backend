package com.mhetre.kitchen.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GupshupWebhookController {
    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody String payload) {
        System.out.println("Received from Gupshup: " + payload);
        // Parse JSON, handle message
        return ResponseEntity.ok("OK");
    }
}