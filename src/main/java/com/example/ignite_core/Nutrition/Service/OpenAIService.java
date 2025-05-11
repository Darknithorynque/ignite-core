package com.example.ignite_core.Nutrition.Service;

import com.example.ignite_core.Nutrition.Model.Entity.MealBoxEntity;
import com.example.ignite_core.Nutrition.Model.Entity.MealEntity;
import com.example.ignite_core.Nutrition.Model.Request.MealRequest;
import com.example.ignite_core.Nutrition.Model.Response.MealResponse;
import com.example.ignite_core.Nutrition.Repository.MealBoxRepository;
import com.example.ignite_core.Nutrition.Repository.MealRepository;
import com.example.ignite_core.Nutrition.Service.NutritionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${spring.application.ai-integration.api-url}")
    private String apiUrl;

    @Value("${spring.application.ai-integration.key}")
    private String apiKey;

    private final MealBoxRepository mealBoxRepository;
    private final MealRepository mealRepository;

    public OpenAIService(MealBoxRepository mealBoxRepository, MealRepository mealRepository){
        this.mealBoxRepository = mealBoxRepository;
        this.mealRepository = mealRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(NutritionService.class);


    private final ObjectMapper objectMapper = new ObjectMapper();

//    public MealResponse getMealResponse(MealRequest mealRequest) throws IOException, ParseException {
//        String systemPrompt = "You are an expert meal categorizer for diets.\n" +
//                "I will give you meal descriptions.\n" +
//                "Your task is to label each meal based on the following rules:\n" +
//                "\n" +
//                "If the meal is healthy and ideal for regular consumption during a diet, label it Green.\n" +
//                "\n" +
//                "If the meal is moderately healthy, balanced, and suitable for occasional consumption during a diet, label it Yellow.\n" +
//                "\n" +
//                "If the meal is unhealthy, not recommended for a diet, or should be consumed rarely or not at all, label it Red.\n" +
//                "\n" +
//                "Special Rule:\n" +
//                "\n" +
//                "If a meal consists of approximately 75% Green foods and 25% Yellow foods, still categorize it as Green.\n" +
//                "\n" +
//                "Output the result in this JSON format:\n" +
//                "\n" +
//                "json\n" +
//                "{\n" +
//                "  \"userId\": \"[just repeat the id what he give you in request]\n" +
//                "  \"label\": \"[0, 1, or 2 because its using enum 0 = green, 1=yellow, 2=red]\",\n" +
//                "   \"calories\": {\n" +
//                "        \"totalCalories\": [totalCal],\n" +
//                "        \"proteins\": [proteins],\n" +
//                "        \"carbohydrates\": [carbs],\n" +
//                "        \"fats\":[fats],\n" +
//                "        \"sugars\": [sugars]\n" +
//                "\n" +
//                "     }, \n" +
//                "   \"mealName\": \"[Repeat the meal description]\",\n" +
//                " \n" +
//                "}\n" +
//                "Only return the JSON. No explanation, no extra text.";
//
//        HttpClient client = HttpClients.createDefault();
//
//        String requestContent = objectMapper.writeValueAsString(Map.of(
//                "userId", mealRequest.getUserId(),
//                "mealName", mealRequest.getMealName()
//        ));
//
//        String payload = objectMapper.writeValueAsString(Map.of(
//                "model", "gpt-4o",
//                "messages", List.of(
//                        Map.of("role", "system", "content", systemPrompt),
//                        Map.of("role", "user", "content", requestContent)
//                ),
//                "temperature", 0.3
//        ));
//
//        HttpPost request = new HttpPost(apiUrl);
//        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
//        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//        request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
//
//        try (CloseableHttpResponse response = (CloseableHttpResponse) client.execute(request)) {
//            String json = EntityUtils.toString(response.getEntity());
//
//            // Extract GPT content from response
//            JsonNode root = objectMapper.readTree(json);
//            String content = root
//                    .path("choices").get(0)
//                    .path("message").path("content").asText();
//
//            MealResponse mealResponse = objectMapper.readValue(content, MealResponse.class);
//
//            System.out.println("OpenAI response: " + mealResponse);
//
//
//            MealEntity meal = new MealEntity();
//            MealBoxEntity mealBox = mealBoxRepository.findByUserId(mealResponse.getUserId()).orElse(null);
//
//            if (mealBox == null) {
//                logger.error("Related meal box has not been found");
//                throw new RuntimeException("User has not related to a meal box");
//            }
//
//            meal.setLabel(mealResponse.getLabel());
//            meal.setCalories(mealResponse.getCalories());
//            meal.setContent(mealResponse.getMealName());
//            meal.setMealBox(mealBox);
//
//            mealBox.getMeals().add(meal);
//
//            mealBoxRepository.save(mealBox);
//
//            return mealResponse;
//
//        }
//    }

    public MealResponse getMealResponse(MealRequest mealRequest) throws IOException, ParseException {
        String systemPrompt = "You are an expert meal categorizer for diets.\n" +
                "I will give you meal descriptions.\n" +
                "Your task is to label each meal based on the following rules:\n" +
                "\n" +
                "If the meal is healthy and ideal for regular consumption during a diet, label it Green.\n" +
                "If the meal is moderately healthy, balanced, and suitable for occasional consumption during a diet, label it Yellow.\n" +
                "If the meal is unhealthy, not recommended for a diet, or should be consumed rarely or not at all, label it Red.\n" +
                "\n" +
                "Special Rule:\n" +
                "If a meal consists of approximately 75% Green foods and 25% Yellow foods, still categorize it as Green.\n" +
                "\n" +
                "Output the result in this JSON format:\n" +
                "{\n" +
                "  \"userId\": \"[just repeat the id what he give you in request]\",\n" +
                "  \"label\": \"[0, 1, or 2 because its using enum 0 = green, 1 = yellow, 2 = red]\",\n" +
                "  \"calories\": {\n" +
                "    \"totalCalories\": [totalCal],\n" +
                "    \"proteins\": [proteins],\n" +
                "    \"carbohydrates\": [carbs],\n" +
                "    \"fats\": [fats],\n" +
                "    \"sugars\": [sugars]\n" +
                "  },\n" +
                "  \"mealName\": \"[Repeat the meal description]\"\n" +
                "}\n" +
                "Only return the JSON. No explanation, no extra text. And pls fill all the fields do not give 0 to calories";

        HttpClient client = HttpClients.createDefault();

        String requestContent = objectMapper.writeValueAsString(Map.of(
                "userId", mealRequest.getUserId(),
                "mealName", mealRequest.getMealName()
        ));

        String payload = objectMapper.writeValueAsString(Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", requestContent)
                ),
                "temperature", 0.3
        ));

        HttpPost request = new HttpPost(apiUrl);
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = (CloseableHttpResponse) client.execute(request)) {
            String json = EntityUtils.toString(response.getEntity());

            System.out.println("RAW OpenAI Response:\n" + json);

            JsonNode root = objectMapper.readTree(json);
            JsonNode messageNode = root.path("choices").get(0).path("message").path("content");

            if (messageNode.isMissingNode()) {
                System.err.println("Error: 'content' field missing in response.");
                throw new RuntimeException("Invalid response from OpenAI - missing 'content'");
            }

            String content = messageNode.asText();
            System.out.println("Extracted GPT content:\n" + content);

            MealResponse mealResponse;
            try {
                mealResponse = objectMapper.readValue(content, MealResponse.class);
            } catch (Exception e) {
                System.err.println("Failed to parse content into MealResponse:\n" + content);
                e.printStackTrace();
                throw new RuntimeException("Failed to parse AI response", e);
            }

            System.out.println("Parsed MealResponse: " + mealResponse);

            MealBoxEntity mealBox = mealBoxRepository.findByUserId(mealResponse.getUserId()).orElse(null);
            if (mealBox == null) {
                System.err.println("MealBox not found for userId: " + mealResponse.getUserId());
                throw new RuntimeException("User does not have a related meal box");
            }

            MealEntity meal = new MealEntity();
            meal.setLabel(mealResponse.getLabel());
            meal.setCalories(mealResponse.getCalories());
            meal.setContent(mealResponse.getMealName());
            meal.setMealBox(mealBox);

            mealBox.getMeals().add(meal);
            mealBoxRepository.save(mealBox);

            System.out.println("Meal saved successfully to mealBox.");

            return mealResponse;
        } catch (IOException e) {
            System.err.println("IO error during API call or response handling");
            e.printStackTrace();
            throw e;
        }
    }
}
