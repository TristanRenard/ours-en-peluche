package com.tristanrenard.ours.controller;

import com.tristanrenard.ours.model.Message;
import com.tristanrenard.ours.model.User;
import com.tristanrenard.ours.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeControllerTl {
    @Autowired
    private HomeService homeService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", homeService.getAllUsers());
        return "users";
    }

    @PostMapping("/submitUser")
    public String submitUser(@RequestParam String name) {
        homeService.createUser(name);
        return "redirect:/messages?userId=" + homeService.getAllUsers().size();
    }

    @GetMapping("/messages")
    public String getMessages(@RequestParam("userId") Integer userId, Model model) {
        User user = homeService.getUserById(userId);
        List<Message> messages = homeService.getMessagesByUserId(userId);

        model.addAttribute("name", user.getName());
        model.addAttribute("messageList", messages.reversed());
        model.addAttribute("userId", userId); // important pour le formulaire de soumission

        return "messages";
    }

    @PostMapping("/submitMessage")
    public String submitMessage(
            @RequestParam("userId") Integer userId,
            @RequestParam("message") String messageContent) {

        homeService.saveMessage(userId, messageContent);

        return "redirect:/messages?userId=" + userId;
    }
}
