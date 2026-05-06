package com.demo.spring_app;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class HelloController {

    private final UserRepository repo;

    public HelloController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String home() {
        return "Mongo Connected 🚀";
    }

    @Value("${ENV:default}")
    private String env;

    @GetMapping("/")
    public String home() {
        return "Sync Service is running in " + env.toUpperCase() + " environment 🚀";
    }
}
