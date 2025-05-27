package org.example.airport.controllers;

import lombok.RequiredArgsConstructor;
import org.example.airport.entity.Flight;
import org.example.airport.services.FlightService;
import org.example.airport.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/flights")
@RequiredArgsConstructor
public class UserFlightController {

    private final FlightService flightService;

    @PutMapping("/{flightId}/register")
    public ResponseEntity<String> registerToFlight(
            @PathVariable Long flightId,
            @AuthenticationPrincipal User user
    ) {
        return flightService.registerToFlight(flightId, user);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Flight>> getMyFlights(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user.getFlights());
    }
}