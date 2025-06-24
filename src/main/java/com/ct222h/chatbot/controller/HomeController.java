package com.ct222h.chatbot.controller;


import com.ct222h.chatbot.response.ApiResponse;
import com.ct222h.chatbot.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final ChatBotService chatBotService;
    @PostMapping()
    public ResponseEntity<ApiResponse> Home(@RequestBody String prompt){
        String res= chatBotService.simpleChat(prompt);
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setMessage(res);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
