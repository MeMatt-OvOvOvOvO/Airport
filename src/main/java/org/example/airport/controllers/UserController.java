package org.example.airport.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Użytkownik", description = "Informacje o zalogowanym użytkowniku")
public class UserController {

    @Operation(
            summary = "Informacje o aktualnie zalogowanym użytkowniku",
            description = "Zwraca nazwę użytkownika, który jest aktualnie zalogowany."
    )
    @GetMapping("/me")
    public String me(@AuthenticationPrincipal UserDetails user) {
        return "Zalogowany jako: " + user.getUsername();
    }
}