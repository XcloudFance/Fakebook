

package dsgp6.fakebook.chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import dsgp6.fakebook.model.User;
import dsgp6.fakebook.model.Message;
import dsgp6.fakebook.repository.UserRepository;
import dsgp6.fakebook.repository.MessageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import dsgp6.fakebook.chat.HashSet;
import java.util.Set;

@Component
public class ChatHandler extends TextWebSocketHandler {
    private static Set<WebSocketSession> sessions = new HashSet<>();

    private final UserRepository userRepository;
    private MessageRepository messageRepository;
    @Autowired
    public ChatHandler(UserRepository userRepository,MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        updateUserOnlineStatus(session.getId(), true);
        System.out.println("New session connected: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        updateUserOnlineStatus(session.getId(), false);
        System.out.println("Session disconnected: " + session.getId());
    }

    private void updateUserOnlineStatus(String sessionId, boolean online) {
        User user = userRepository.getByUid(sessionId);
        if (user != null) {
            user.setOnline(online);
            userRepository.save(user);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message from " + session.getId() + ": " + message.getPayload());
        broadcast(message.getPayload());
    }

    private void broadcast(String message) throws IOException {
        // 解析消息中的参数
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonMessage = objectMapper.readTree(message);
        String senderId = jsonMessage.get("senderId").asText();
        String recipientId = jsonMessage.get("recipientId").asText();
        String content = jsonMessage.get("content").asText();

        // 在消息存入MongoDB之前，先查找发送者和接收者的在线状态
        User sender = userRepository.getByUid(senderId);
        User recipient = userRepository.getByUid(recipientId);

        // 将消息存入MongoDB
        Message messageObj = new Message();

        messageObj.setSenderId(senderId);
        messageObj.setRecipientId(recipientId);
        messageObj.setContent(content);
        messageRepository.save(messageObj);

        // 如果接收者在线，直接推送消息
        if (recipient != null && recipient.isOnline()) {
            for (WebSocketSession session : sessions) {
                if (session.getId().equals(recipientId)) {
                    session.sendMessage(new TextMessage(content));
                    break;
                }
            }
        }
    }
}
