package com.flights.project.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAiTravelAdvice(String userPrompt) {
        
        String systemInstruction = "You are an expert AI Travel Agent. Your job is to provide smart, detailed, and amazing travel itineraries, flight advice, and entertainment recommendations. " +
                "CRITICAL RULE: You must ONLY answer questions directly related to flights, travel, vacations, destinations, hotels, trips, and entertainment/attractions. " +
                "If the user asks about anything else (such as cooking, coding, math, general science, or history not about a tourist destination), " +
                "you must politely refuse to answer and say exactly this in Hebrew: 'אני סוכן ה-AI שלכם לטיסות וחופשות בלבד, אשמח לעזור לכם לתכנן את היעד הבא שלכם!'";

        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini"); // המודל הכי מהיר וחכם למשימות כאלו

        List<Map<String, String>> messages = new ArrayList<>();
        
        
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemInstruction);
        messages.add(systemMessage);

        
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);
        messages.add(userMessage);

        requestBody.put("messages", messages);

        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            
            Map<String, Object> response = restTemplate.postForObject(apiUrl, entity, Map.class);

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, String> message = (Map<String, String>) firstChoice.get("message");
                    return message.get("content");
                }
            }
            return "משהו השתבש בקבלת המידע מהסוכן המרכזי.";
            
        } catch (Exception e) {
            return "שגיאה בתקשורת עם סוכן ה-AI: " + e.getMessage();
        }
    }
}
