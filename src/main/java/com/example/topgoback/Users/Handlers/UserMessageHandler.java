package com.example.topgoback.Users.Handlers;

import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UserMessageHandler implements WebSocketHandler {


    public static final ConcurrentHashMap<String, WebSocketSession> userSession = new ConcurrentHashMap<>();


    public static void addSession(WebSocketSession session) {
        HttpHeaders headers = session.getHandshakeHeaders();
        String id = headers.get("id").get(0);
        userSession.put(id,session);
    }

    public static void removeSession(WebSocketSession session) {
        HttpHeaders headers = session.getHandshakeHeaders();
        String id = headers.get("id").get(0);
        userSession.remove(id);
    }


    public static void sendUserMessage(List<WebSocketSession> sessions, UserMessagesDTO update){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        if(sessions.isEmpty()){
            return;
        }
        try {
            TextMessage textMessage = new TextMessage(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(update));
            for(WebSocketSession webSocketSession:sessions){
                if(webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(textMessage);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        addSession(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        removeSession(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
