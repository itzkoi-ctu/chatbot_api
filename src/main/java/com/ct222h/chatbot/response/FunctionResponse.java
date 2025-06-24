package com.ct222h.chatbot.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionResponse {
    private String currencyName;
    private String functionName;
    private String currencyData;
    private String textResponse;
}
