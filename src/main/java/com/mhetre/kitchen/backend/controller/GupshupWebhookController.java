package com.mhetre.kitchen.backend.controller;

import com.mhetre.kitchen.backend.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Gupshup usually sends form data or JSON, check your webhook settings
    @PostMapping("/incoming")
    @GetMapping("/incoming")
    public ResponseEntity<String> receiveMessageGet(@RequestParam("source") String phone,
                                                    @RequestParam("message") String message) {
        whatsAppService.handleIncomingMessage(phone, message);
       // return ResponseEntity.ok("received");
        // Message text sent by user

        if (phone != null && message != null) {
            // Handle the incoming message
            whatsAppService.handleIncomingMessage(phone, message);
            // Respond to Gupshup with 200 OK (empty or "received")
            return ResponseEntity.ok("received");
        }

        return ResponseEntity.badRequest().body("Invalid request");
    }
}

