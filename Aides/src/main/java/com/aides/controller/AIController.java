package com.aides.controller;

import com.aides.service.AIParliamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {
    
    private final AIParliamentService aiParliamentService;

    public AIController(AIParliamentService aiParliamentService) {
        this.aiParliamentService = aiParliamentService;
    }

    @PostMapping("/ask")
    public ResponseEntity<?> askQuestion(@RequestBody String question) {
        try {
            String answer = aiParliamentService.processQuestion(question);
            return ResponseEntity.ok(answer);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing question");
        }
    }
}