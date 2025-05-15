package com.cadu.api_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class ChatController {

    private final ChatClient chatCliente;

    public ChatController(@NotNull ChatClient.Builder chatClienteBuilder) {
        this.chatCliente = chatClienteBuilder.build();
    }

    @GetMapping("/ai")
    String generation(String userInput){
        return this.chatCliente.prompt().user(userInput).call().content();
    }

    @GetMapping("/teste")
    String teste(String userInput){
        return userInput;
    }
}
