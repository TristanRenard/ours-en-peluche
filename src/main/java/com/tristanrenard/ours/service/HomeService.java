package com.tristanrenard.ours.service;

import com.tristanrenard.ours.model.Message;
import com.tristanrenard.ours.model.User;
import com.tristanrenard.ours.repository.MessageRepository;
import com.tristanrenard.ours.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class HomeService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(String name) {
        User user = new User(name);
        userRepository.save(user);
    }


    public List<Message> getMessagesByUserId(Integer userId) {
        User user = getUserById(userId);
        return user.getMessages();
    }

    public void saveMessage(Integer userId, String messageContent, Boolean isBot) {
        User user = getUserById(userId);
        Message message = new Message();
        message.setUser(user);
        message.setMessage(messageContent);
        message.setDate(new Timestamp(System.currentTimeMillis()));
        message.setBot(isBot);

        messageRepository.save(message);
    }


}
