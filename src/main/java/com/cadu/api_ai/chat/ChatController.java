package com.cadu.api_ai.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatCliente;

    public ChatController(@NotNull ChatClient.Builder chatClienteBuilder) {
        this.chatCliente = chatClienteBuilder.build();
    }
    @PostMapping
    ChatMessage generationMessage(@RequestBody ChatMessage message){
        String response = this.chatCliente.prompt().user(message.message()).call().content();
        return new ChatMessage(response);
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
