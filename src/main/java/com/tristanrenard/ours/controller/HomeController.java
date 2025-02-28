package com.tristanrenard.ours.controller;

import com.tristanrenard.ours.service.HomeService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService){
        this.homeService = homeService;
    }

}
