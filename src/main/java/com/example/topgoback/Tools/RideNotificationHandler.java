package com.example.topgoback.Tools;

import com.example.topgoback.Rides.DTO.RideDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RideNotificationHandler implements WebSocketHandler {


    public static final ConcurrentHashMap<String, WebSocketSession> driverSessions = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, WebSocketSession> passengerSessions = new ConcurrentHashMap<>();


    public static void addSession(WebSocketSession session) {
        HttpHeaders headers = session.getHandshakeHeaders();
        String id = headers.get("id").get(0);
        String role = headers.get("role").get(0);

        if (role.equals("DRIVER")) {
            driverSessions.put(id, session);
        } else if (role.equals("USER")) {
            passengerSessions.put(id, session);
        }
    }

    public static void removeSession(WebSocketSession session) {
        HttpHeaders headers = session.getHandshakeHeaders();
        String id = headers.get("id").get(0);
        String role = headers.get("role").get(0);

        if (role.equals("DRIVER")) {
            driverSessions.remove(id);
        } else if (role.equals("USER")) {
            passengerSessions.remove(id);
        }
    }




    public static void notifyPassengerAboutScheduledRide(List<WebSocketSession> sessions, String message) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        try {
            TextMessage textMessage = new TextMessage(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(message));
            for(WebSocketSession webSocketSession:sessions){
                webSocketSession.sendMessage(textMessage);
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
