package com.flights.project.controllers;

import com.flights.project.services.OpenAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    private final OpenAIService openAIService;

    public AIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chatWithAgent(@RequestBody Map<String, String> request) {
        String userPrompt = request.get("prompt");
        Map<String, String> response = new HashMap<>();

        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            response.put("error", "ההודעה אינה יכולה להיות ריקה");
            return ResponseEntity.badRequest().body(response);
        }

        String aiResponse = openAIService.getAiTravelAdvice(userPrompt);
        response.put("response", aiResponse);
        
        return ResponseEntity.ok(response);
    }
}
