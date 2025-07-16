package com.cadu.api_ai.memory;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-memory")
public class MemoryChatController {

    private final MemoryChatService memoryChatService;

    public MemoryChatController(MemoryChatService memoryChatService) {
        this.memoryChatService = memoryChatService;
    }

    @PostMapping("/{chatId}")
    ChatMessage simpleChat(@PathVariable String chatId,  @RequestBody ChatMessage message){
        String response = this.memoryChatService.simpleChat(message.content(), chatId);
        return new ChatMessage(response, "ASSISTANT");
    }

    @PostMapping("/start")
    NewChatResponse startNewChat(@RequestBody ChatMessage chatMessage){
        return this.memoryChatService.createNewChatResponse(chatMessage.content());
    }

    @GetMapping
    List<Chat> getAllChatsForUser(){
        return memoryChatService.getAllChatsForUser();
    }

    @GetMapping("/{chatId}")
    public List<ChatMessage> getChatMessages(@PathVariable String chatId) {
        return this.memoryChatService.getAllChatsMessages(chatId);
    }


}
