package com.demo.spring_app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class HelloController {

    private final UserRepository repo;

    // Read ENV from Cloud Run
    @Value("${ENV:local}")
    private String env;

    // Optional: build number for debugging
    @Value("${BUILD_NUMBER:unknown}")
    private String build;

    public HelloController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String home() {

        String message = "";

        if (env.equalsIgnoreCase("qa")) {
            message = "🧪 QA Environment - Sync Service Running";
        } else if (env.equalsIgnoreCase("staging")) {
            message = "🟡 STAGING Environment - Pre-Production";
        } else if (env.equalsIgnoreCase("prod")) {
            message = "🚀 PRODUCTION Environment - Live System";
        } else {
            message = "💻 LOCAL Environment";
        }

        return message + " | Build: " + build;
    }

    @GetMapping("/db-test")
    public String testDB() {
        return "DB Connected Successfully in " + env.toUpperCase() + " 🚀";
    }

    @GetMapping("/health")
    public String health() {
        return "OK - " + env.toUpperCase();
    }
}
