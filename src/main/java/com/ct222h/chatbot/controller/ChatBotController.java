package com.ct222h.chatbot.controller;

import com.ct222h.chatbot.dto.PromptBody;
import com.ct222h.chatbot.response.ApiResponse;
import com.ct222h.chatbot.service.IChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
public class ChatBotController {
    private final IChatBotService chatBotService;

    @PostMapping()
    public ResponseEntity<ApiResponse> getCoinDetails(@RequestBody PromptBody prompt) throws Exception {
        ApiResponse response= chatBotService.getCoinDetails(prompt.getPrompt());


        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/simple")
    public ResponseEntity<ApiResponse> simpleChatHandler(@RequestBody PromptBody prompt) throws Exception {
        String response= chatBotService.simpleChat(prompt.getPrompt());

        ApiResponse rs = new ApiResponse();
        rs.setMessage(response);
        return  new ResponseEntity<>(rs, HttpStatus.OK);
    }
}
