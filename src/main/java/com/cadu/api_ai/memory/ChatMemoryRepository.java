package com.cadu.api_ai.memory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class ChatMemoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChatMemoryRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generateChatId(String userId, String description) {
        final String sql = "INSERT INTO chat_memory (user_id, description) VALUES (?, ?) RETURNING conversation_id ";
        return jdbcTemplate.queryForObject(sql, String.class, userId, description);
    }

    public List<Chat> getAllChatsForUSer(String userId) {
        String sql = "SELECT conversation_id, user_id, description FROM chat_memory where user_id = ?";
        return jdbcTemplate.query(sql,  (rs, rowNum) ->  new Chat(rs.getString("conversation_id"), rs.getString("description")), userId);
    }

    public List<ChatMessage> getAllChatsMessages(String chatId){
        String sql = "SELECT content, type FROM spring_ai_chat_memory WHERE conversation_id = ? order by timestamp ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ChatMessage(rs.getString("content"), rs.getString("type")), chatId);
    }
}
