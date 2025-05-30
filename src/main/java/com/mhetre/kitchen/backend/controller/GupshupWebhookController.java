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

    // ✅ Real Gupshup POST webhook (used in production)
    @PostMapping("/incoming")
    public ResponseEntity<String> receiveMessagePost(@RequestParam Map<String, String> body) {
        String phone = body.get("source");
        String message = body.get("message");

        if (phone != null && message != null) {
            whatsAppService.handleIncomingMessage(phone, message);
            return ResponseEntity.ok("received");
        }

        return ResponseEntity.badRequest().body("Invalid request");
    }

    // ✅ Temporary GET support for testing via browser
    @GetMapping("/incoming")
    public ResponseEntity<String> receiveMessageGet(@RequestParam("source") String phone,
                                                    @RequestParam("message") String message) {
        whatsAppService.handleIncomingMessage(phone, message);
        return ResponseEntity.ok("received");
    }
}
