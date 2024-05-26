package com.example.WeeSeed;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ImageWebSocketHandler imageWebSocketHandler;

    public WebSocketConfig(ImageWebSocketHandler imageWebSocketHandler) {
        this.imageWebSocketHandler = imageWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(imageWebSocketHandler, "/ws/image-upload").setAllowedOrigins("*");
    }
}
