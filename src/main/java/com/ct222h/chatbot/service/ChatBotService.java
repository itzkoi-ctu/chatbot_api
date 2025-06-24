package com.ct222h.chatbot.service;

import com.ct222h.chatbot.dto.CoinDto;
import com.ct222h.chatbot.response.ApiResponse;
import com.ct222h.chatbot.response.FunctionResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ChatBotService implements IChatBotService {
    private static final Logger LOGGER = Logger.getLogger(ChatBotService.class.getName());

    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;
    @Value("${COINGECKO_API_URL}")
    private String coingeckoApiUrl;
    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + value.getClass().getName());
        }
    }

//    public CoinDto makeApiRequest(String currencyName) throws Exception {
//        String url = coingeckoApiUrl + currencyName.toLowerCase();
//        LOGGER.info("Making CoinGecko API request for currency: " + currencyName);
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//        ResponseEntity<Map> responseEntity;
//        try {
//            responseEntity = restTemplate.getForEntity(url, Map.class);
//        } catch (Exception e) {
//            throw new Exception("Failed to fetch data from CoinGecko for " + currencyName + ": " + e.getMessage());
//        }
//        Map<String, Object> responseBody = responseEntity.getBody();
//
//        if (responseBody != null) {
//            Map<String, Object> image = (Map<String, Object>) responseBody.get("image");
//            Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");
//
//            CoinDto coinDto = new CoinDto();
//            coinDto.setId((String) responseBody.get("id"));
//            coinDto.setName((String) responseBody.get("name"));
//            coinDto.setSymbol((String) responseBody.get("symbol"));
//            coinDto.setImage((String) image.get("large"));
//            coinDto.setCurrentPrice(convertToDouble(((Map<String, Object>) marketData.get("current_price")).get("usd")));
//            coinDto.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap")).get("usd")));
//            coinDto.setMarketCapRank(convertToDouble((Integer) marketData.get("market_cap_rank")));
//            coinDto.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).get("usd")));
//            coinDto.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
//            coinDto.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));
//            coinDto.setPriceChange24h(convertToDouble(marketData.get("price_change_24h")));
//            coinDto.setPriceChangePercentage24h(convertToDouble(marketData.get("price_change_percentage_24h")));
//            coinDto.setMarketCapChange24h(convertToDouble(marketData.get("market_cap_change_24h")));
//            coinDto.setMarketCapChangePercentage24h(convertToDouble(marketData.get("market_cap_change_percentage_24h")));
//            coinDto.setCirculatingSupply(convertToDouble(marketData.get("circulating_supply")));
//            coinDto.setTotalSupply(convertToDouble(marketData.get("total_supply")));
//
//            LOGGER.info("CoinGecko data for " + currencyName + ": " + new JSONObject(coinDto).toString());
//            return coinDto;
//        }
//        throw new Exception("Coin not found: " + currencyName);
//    }
public CoinDto makeApiRequest(String currencyName) throws Exception {
    String url = coingeckoApiUrl + currencyName.toLowerCase();
    LOGGER.info("Making CoinGecko API request for currency: " + currencyName);

    HttpHeaders headers = new HttpHeaders();
    headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
    HttpEntity<String> entity = new HttpEntity<>(headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map> responseEntity;
    try {
        responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );
    } catch (Exception e) {
        throw new Exception("Failed to fetch data from CoinGecko for " + currencyName + ": " + e.getMessage());
    }

    Map<String, Object> responseBody = responseEntity.getBody();

    if (responseBody != null) {
        Map<String, Object> image = (Map<String, Object>) responseBody.get("image");
        Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");

        CoinDto coinDto = new CoinDto();
        coinDto.setId((String) responseBody.get("id"));
        coinDto.setName((String) responseBody.get("name"));
        coinDto.setSymbol((String) responseBody.get("symbol"));
        coinDto.setImage((String) image.get("large"));
        coinDto.setCurrentPrice(convertToDouble(((Map<String, Object>) marketData.get("current_price")).get("usd")));
        coinDto.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap")).get("usd")));
        coinDto.setMarketCapRank(convertToDouble((Integer) marketData.get("market_cap_rank")));
        coinDto.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).get("usd")));
        coinDto.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
        coinDto.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));
        coinDto.setPriceChange24h(convertToDouble(marketData.get("price_change_24h")));
        coinDto.setPriceChangePercentage24h(convertToDouble(marketData.get("price_change_percentage_24h")));
        coinDto.setMarketCapChange24h(convertToDouble(marketData.get("market_cap_change_24h")));
        coinDto.setMarketCapChangePercentage24h(convertToDouble(marketData.get("market_cap_change_percentage_24h")));
        coinDto.setCirculatingSupply(convertToDouble(marketData.get("circulating_supply")));
        coinDto.setTotalSupply(convertToDouble(marketData.get("total_supply")));

        LOGGER.info("CoinGecko data for " + currencyName + ": " + new JSONObject(coinDto).toString());
        return coinDto;
    }

    throw new Exception("Coin not found: " + currencyName);
}


    public FunctionResponse getFunctionResponse(String prompt) {
        log.info("GEMINI API KEY: "+ geminiApiKey);
        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;
        FunctionResponse functionResponse = new FunctionResponse();
        functionResponse.setCurrencyName(""); // Initialize to avoid null
        try {
            JSONObject requestBodyJson = new JSONObject()
                    .put("contents", new JSONArray()
                            .put(new JSONObject()
                                    .put("parts", new JSONArray()
                                            .put(new JSONObject().put("text", prompt)))))
                    .put("tools", new JSONArray()
                            .put(new JSONObject()
                                    .put("functionDeclarations", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("name", "getCoinDetails")
                                                    .put("description", "Retrieve detailed information about a cryptocurrency (e.g., market cap, price, rank) by its name, id, or symbol (e.g., bitcoin, eth).")
                                                    .put("parameters", new JSONObject()
                                                            .put("type", "OBJECT")
                                                            .put("properties", new JSONObject()
                                                                    .put("currencyName", new JSONObject()
                                                                            .put("type", "STRING")
                                                                            .put("description", "The cryptocurrency name, id, or symbol (e.g., bitcoin, ethereum, eth)."))
                                                                    .put("currencyData", new JSONObject()
                                                                            .put("type", "STRING")
                                                                            .put("description", "Optional JSON string containing cryptocurrency details (e.g., id, symbol, market cap).")))
                                                            .put("required", new JSONArray()
                                                                    .put("currencyName")))))));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

            String responseBody = response.getBody();
            LOGGER.info("Gemini API response: " + responseBody);
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray candidates = jsonObject.getJSONArray("candidates");

            if (candidates.isEmpty()) {
                LOGGER.warning("No candidates returned from Gemini for prompt: " + prompt);
                return functionResponse;
            }

            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            JSONObject firstPart = parts.getJSONObject(0);

            if (firstPart.has("functionCall")) {
                JSONObject functionCall = firstPart.getJSONObject("functionCall");
                String functionName = functionCall.getString("name");
                JSONObject args = functionCall.getJSONObject("args");
                String currencyName = args.optString("currencyName", "");
                String currencyData = args.optString("currencyData", "");

                LOGGER.info("Function call detected: " + functionName + ", Currency: " + currencyName);
                functionResponse.setFunctionName(functionName);
                functionResponse.setCurrencyName(currencyName);
                functionResponse.setCurrencyData(currencyData);
                return functionResponse;
            } else {
//                String modelText = firstPart.optString("text", "No text response.");
//                LOGGER.info("No functionCall returned. Gemini said: " + modelText);
//
//                // Extract currency name from prompt or Gemini response using regex
//                String currencyName = null;
//                String promptLower = prompt.toLowerCase();
//                String modelTextLower = modelText.toLowerCase();
//
//                // Regex pattern to match common cryptocurrency names or symbols
//                String regex = "\\b(bitcoin|ethereum|tether|binancecoin|solana|ripple|dogecoin|cardano|avalanche|tron|eth|btc|xrp|doge|ada|bnb)\\b";
//                Pattern pattern = Pattern.compile(regex);
//                Matcher matcher = pattern.matcher(promptLower + " " + modelTextLower);
//
//                if (matcher.find()) {
//                    currencyName = matcher.group();
//                    // Map aliases to CoinGecko IDs
//                    currencyName = switch (currencyName) {
//                        case "eth" -> "ethereum";
//                        case "btc" -> "bitcoin";
//                        case "xrp" -> "ripple";
//                        case "doge" -> "dogecoin";
//                        case "ada" -> "cardano";
//                        case "bnb" -> "binancecoin";
//                        default -> currencyName;
//                    };
//                }
//
//                functionResponse.setFunctionName("getCoinDetails");
////                functionResponse.setCurrencyName(currencyName != null ? currencyName : "");
//                if ( currencyName != null) {
//                    functionResponse.setCurrencyName(currencyName);
//                    LOGGER.info("Extracted currency name: " + currencyName);
//                } else {
//                    functionResponse.setCurrencyName("");
//                    LOGGER.info("No currency name found in prompt or model response.");
//                }
//                LOGGER.info("Extracted currency name: " + functionResponse.getCurrencyName());
//                return functionResponse;
                // ELSE – không có functionCall
                String modelText = firstPart.optString("text", "No text response.");
                LOGGER.info("No functionCall returned. Gemini said: " + modelText);

// Regex tìm tên coin
                String currencyName = null;
                String promptLower = prompt.toLowerCase();
                String modelTextLower = modelText.toLowerCase();

                String regex = "\\b(bitcoin|ethereum|tether|binancecoin|solana|ripple|dogecoin|cardano|avalanche|tron|eth|btc|xrp|doge|ada|bnb)\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(promptLower + " " + modelTextLower);

                if (matcher.find()) {
                    currencyName= normalizeCoinName(matcher.group());
                    // Nếu tìm được coin → xử lý như bình thường
                    functionResponse.setFunctionName("getCoinDetails");
                    functionResponse.setCurrencyName(currencyName);
                    LOGGER.info("Extracted currency name (regex): " + currencyName);
                } else {
                    // Nếu không tìm được coin nào → chỉ trả lời văn bản
                    functionResponse.setFunctionName(null); // không gọi gì cả
                    functionResponse.setCurrencyName("");
                    functionResponse.setTextResponse(modelText); // dùng để frontend render phản hồi
                    LOGGER.info("No currency name found → returning model text.");
                }
                return functionResponse;
            }
        } catch (Exception e) {
            LOGGER.severe("Error in getFunctionResponse: " + e.getMessage());
            e.printStackTrace();
            functionResponse.setCurrencyName("");
            return functionResponse;
        }
    }

    @Override
    public ApiResponse getCoinDetails(String prompt) throws Exception {
        LOGGER.info("Processing getCoinDetails for prompt: " + prompt);
        // Step 1: Get function response from Gemini
        FunctionResponse functionResponse = getFunctionResponse(prompt);
        if (functionResponse == null || functionResponse.getCurrencyName() == null || functionResponse.getCurrencyName().isEmpty()) {
            if (functionResponse != null && functionResponse.getTextResponse() != null && !functionResponse.getTextResponse().isEmpty()) {
                LOGGER.info("Returning textResponse from Gemini.");
                ApiResponse response = new ApiResponse();
                response.setMessage(functionResponse.getTextResponse());
                return response;
            }

            // Nếu không có cả currencyName lẫn textResponse → báo lỗi
            LOGGER.warning("Unable to determine currency name or generate response.");
            ApiResponse response = new ApiResponse();
            response.setMessage("Unable to determine cryptocurrency name from prompt or Gemini response. Please specify a valid cryptocurrency (e.g., Bitcoin, Ethereum).");
            return response;
        }


        // Step 2: Fetch coin data from CoinGecko
        String currencyName = functionResponse.getCurrencyName();
        CoinDto coinDto;
        try {
            coinDto = makeApiRequest(currencyName);
        } catch (Exception e) {
            LOGGER.severe("Failed to fetch coin data for " + currencyName + ": " + e.getMessage());
            ApiResponse response = new ApiResponse();
            response.setMessage("Failed to fetch data for " + currencyName + ": " + e.getMessage());
            return response;
        }

        // Step 3: Prepare Gemini API request with coin data
        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("role", "user")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt))))
                        .put(new JSONObject()
                                .put("role", "model")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("functionCall", new JSONObject()
                                                        .put("name", "getCoinDetails")
                                                        .put("args", new JSONObject()
                                                                .put("currencyName", currencyName)
                                                                .put("currencyData", new JSONObject(coinDto).toString()))))))
                        .put(new JSONObject()
                                .put("role", "function")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("functionResponse", new JSONObject()
                                                        .put("name", "getCoinDetails")
                                                        .put("response", new JSONObject()
                                                                .put("name", "getCoinDetails")
                                                                .put("content", new JSONObject(coinDto))))))))
                .put("tools", new JSONArray()
                        .put(new JSONObject()
                                .put("functionDeclarations", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", "getCoinDetails")
                                                .put("description", "Retrieve detailed information about a cryptocurrency (e.g., market cap, price, rank) by its name, id, or symbol (e.g., bitcoin, eth).")
                                                .put("parameters", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("properties", new JSONObject()
                                                                .put("currencyName", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description", "The cryptocurrency name, id, or symbol (e.g., bitcoin, ethereum, eth)."))
                                                                .put("currencyData", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description", "Optional JSON string containing cryptocurrency details (e.g., id, symbol, market cap).")))
                                                        .put("required", new JSONArray()
                                                                .put("currencyName")))))))
                .toString();

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(GEMINI_API_URL, request, String.class);
        } catch (Exception e) {
            LOGGER.severe("Failed to communicate with Gemini API: " + e.getMessage());
            ApiResponse responseError = new ApiResponse();
            responseError.setMessage("Failed to communicate with Gemini API: " + e.getMessage());
            return responseError;
        }

        String responseBody = response.getBody();
        LOGGER.info("Gemini final response: " + responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray candidates = jsonObject.getJSONArray("candidates");

        if (candidates.isEmpty()) {
            LOGGER.warning("No candidates returned from Gemini for prompt: " + prompt);
            ApiResponse responseError = new ApiResponse();
            responseError.setMessage("No response from Gemini. Please try again.");
            return responseError;
        }

        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONObject content = firstCandidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        JSONObject firstPart = parts.getJSONObject(0);
        String text = firstPart.optString("text", "No text response.");

        ApiResponse ans = new ApiResponse();
        ans.setMessage(text);
        LOGGER.info("Final response message: " + text);
        return ans;
    }

    @Override
    public String simpleChat(String prompt) {
        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt)))))
                .toString();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

        return response.getBody();
    }
    private String normalizeCoinName(String coin) {
        return switch (coin) {
            case "eth" -> "ethereum";
            case "btc" -> "bitcoin";
            case "xrp" -> "ripple";
            case "doge" -> "dogecoin";
            case "ada" -> "cardano";
            case "bnb" -> "binancecoin";
            default -> coin;
        };
    }

}