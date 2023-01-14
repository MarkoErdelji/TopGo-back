package com.example.topgoback.Rides.Controller;

import com.example.topgoback.Rides.DTO.RideDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CreateRideHandler implements WebSocketHandler {


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



    public static void notifyDriver(WebSocketSession session, RideDTO rideDTO) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            TextMessage textMessage = new TextMessage(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rideDTO));
            session.sendMessage(textMessage);
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
