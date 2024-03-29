package com.example.topgoback.Tools;

import com.example.topgoback.Users.Handlers.UserMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(
                "/topic/driver/ride/",
                "/topic/passenger/ride/",
                "/topic/user/message/",
                "/topic/passenger/scheduledNotification/",
                "/topic/vehicleLocation/ride/user/",
                "/topic/passenger/invites/",
                "/topic/passenger/response/",
                "/topic/work-hour/driver/");

        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CreateRideHandler(), "/websocket");
        registry.addHandler(new SimulationHandler(),"/simulation");
        registry.addHandler(new UserMessageHandler(),"/messages");
        registry.addHandler(new RideNotificationHandler(),"/notificationSocket");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS();
    }



}