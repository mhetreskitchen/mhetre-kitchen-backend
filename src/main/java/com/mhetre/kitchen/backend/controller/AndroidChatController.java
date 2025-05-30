package com.mhetre.kitchen.backend.controller;

import com.mhetre.kitchen.backend.service.AndroidChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/android")
public class AndroidChatController {

    @Autowired
    private AndroidChatService chatService;

    @PostMapping("/message")
    public ResponseEntity<String> receiveChat(@RequestParam String phone, @RequestParam String message) {
        String reply = chatService.handleMessage(phone, message);
        return ResponseEntity.ok(reply);
    }
}
