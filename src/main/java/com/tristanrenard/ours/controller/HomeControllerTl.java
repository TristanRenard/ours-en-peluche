package com.tristanrenard.ours.controller;

import com.tristanrenard.ours.model.Message;
import com.tristanrenard.ours.model.User;
import com.tristanrenard.ours.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Controller
public class HomeControllerTl {

    @Value("${bot.quote.api.url}")
    private String quoteApiUrl;

    @Autowired
    private HomeService homeService;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", homeService.getAllUsers());
        return "users";
    }

    @PostMapping("/submitUser")
    public String submitUser(@RequestParam String name) {
        homeService.createUser(name);

        List<User> users = homeService.getAllUsers();
        users.sort((u1, u2) -> u2.getId() - u1.getId());
        Integer maxId = users.get(0).getId();

        return "redirect:/messages?userId=" + maxId;
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



        homeService.saveMessage(userId, messageContent, false);

        String quoteUrl = quoteApiUrl;
        Map<String, String> response = restTemplate.getForObject(quoteUrl, Map.class);

        if (response != null && response.containsKey("quote")) {
            String botMessage = response.get("quote");

            homeService.saveMessage(userId, botMessage, true);
        }

        return "redirect:/messages?userId=" + userId;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/users";
    }
}
