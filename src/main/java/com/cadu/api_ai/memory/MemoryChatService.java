package com.cadu.api_ai.memory;

import com.cadu.api_ai.memory.ChatMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Service
public class MemoryChatService {

    private final String USER_ID = "Cadu";
    private final String DESCRIPTION_PROMPT =  "Generate a chat description based on the message, limiting the description to 30 caracters:";

    private final ChatClient chatCliente;

    private final ChatMemoryRepository chatMemoryRepository;

    public MemoryChatService(ChatClient.Builder chatClienteBuilder, JdbcChatMemoryRepository jdbcChatMemoryRepository, ChatMemoryRepository chatMemoryRepository) {
        this.chatMemoryRepository = chatMemoryRepository;
        ChatMemory chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(jdbcChatMemoryRepository).maxMessages(10).build();
        this.chatCliente = chatClienteBuilder.defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory).build(),
                new SimpleLoggerAdvisor()
        ).build() ;
    }



    public NewChatResponse createNewChatResponse(String message){
        String description = this.generateDescription(message);
        String chatId = this.chatMemoryRepository.generateChatId(USER_ID, description);
        String response = this.simpleChat(message, chatId);
        return new NewChatResponse(chatId, description, response);
    }

    public String simpleChat(String chatMessage, String chatId){
       return this.chatCliente.prompt().advisors(a -> a.param( ChatMemory.CONVERSATION_ID, chatId)).user(chatMessage).call().content();
    }

    private String generateDescription(String message){
        final String prompt = DESCRIPTION_PROMPT ;
        return this.chatCliente.prompt().user(prompt + message).call().content();
    }


    public List<Chat> getAllChatsForUser(){
        return this.chatMemoryRepository.getAllChatsForUSer(USER_ID);
    }

    public List<ChatMessage> getAllChatsMessages(String chatId){
        return this.chatMemoryRepository.getAllChatsMessages(chatId);
    }
}
