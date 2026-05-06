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
        return "Hello world 🚀";
    }

    @GetMapping("/db-test")
    public String testDB() {
        return "DB Connected Successfully 🚀";
}

}
