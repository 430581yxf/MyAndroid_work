package com.example.demo.config.webSocket;
import com.example.demo.Utils.Utils;
import com.example.demo.entity.Message;
import com.example.demo.entity.MessageImage;
import com.example.demo.entity.User;
import com.example.demo.service.ImageService;
import com.example.demo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    private MessageService messageService;
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    @Autowired
    public MyWebSocketHandler(MessageService messageService) {
        this.messageService = messageService;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        HttpHeaders handshakeHeaders = session.getHandshakeHeaders();
        List<String> user = handshakeHeaders.get("user");
        String s = user.get(0);
        System.out.println(session+"\ns:"+s);
        sessions.add(session);

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        TextMessage textMessage = (TextMessage) message;
        String payload = textMessage.getPayload();// 处理收到的消息
        ObjectMapper objectMapper = new ObjectMapper();
        Message message1=objectMapper.readValue(payload,Message.class);
        int i=0;
        for (i = 0; i < sessions.size(); i++) {
            System.out.println(i);
            WebSocketSession webSocketSession = sessions.get(i);
            HttpHeaders handshakeHeaders = webSocketSession.getHandshakeHeaders();
            List<String> user1 = handshakeHeaders.get("user");
            String s = user1.get(0);
            System.out.println("i:"+i);
            User user=objectMapper.readValue(s,User.class);
            if(user.getId().equals(message1.getReceiverId())){
                TextMessage reply = new TextMessage(payload);
                webSocketSession.sendMessage(reply);
                break;
            }

        }
        if(i==sessions.size()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    messageService.save(message1);
                }
            }).start();
        }
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        sessions.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}