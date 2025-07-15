package com.cadu.api_ai.memory;

import com.cadu.api_ai.chat.ChatMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class MemoryChatService {

    private final ChatClient chatCliente;

    public MemoryChatService(ChatClient.Builder chatClienteBuilder) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();
        this.chatCliente = chatClienteBuilder.defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory).build(),
                new SimpleLoggerAdvisor()
        ).build() ;
    }

    public String simpleChat(String chatMessage){
       return this.chatCliente.prompt().advisors(a -> a.param( ChatMemory.CONVERSATION_ID, "123456")).user(chatMessage).call().content();
    }
}
