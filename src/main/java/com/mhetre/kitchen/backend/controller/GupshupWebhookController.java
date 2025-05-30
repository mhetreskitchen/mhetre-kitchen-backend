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

    @PostMapping("/gupshup/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        try {
            Map<String, Object> messageObj = (Map<String, Object>) ((List<?>) payload.get("payload")).get(0);
            String phone = (String) messageObj.get("phone");
            String message = (String) messageObj.get("text");

            whatsAppService.handleIncomingMessage(phone, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Message received");
    }
}
