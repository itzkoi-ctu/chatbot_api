package com.ct222h.chatbot.service;

import com.ct222h.chatbot.response.ApiResponse;

public interface IChatBotService {
    public ApiResponse getCoinDetails(String prompt) throws Exception;

    public String simpleChat(String prompt);


}
