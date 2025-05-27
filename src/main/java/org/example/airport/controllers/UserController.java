package org.example.airport.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public String me(@AuthenticationPrincipal UserDetails user) {
        return "Zalogowany jako: " + user.getUsername();
    }
}