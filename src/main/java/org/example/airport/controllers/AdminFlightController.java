package org.example.airport.controllers;

import lombok.RequiredArgsConstructor;
import org.example.airport.entity.Flight;
import org.example.airport.entity.User;
import org.example.airport.services.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/flights")
@RequiredArgsConstructor
public class AdminFlightController {

    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<Flight> create(@RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.addFlight(flight));
    }

    @GetMapping("/{flightId}/passengers")
    public ResponseEntity<List<User>> getPassengers(@PathVariable Long flightId) {
        return ResponseEntity.ok(flightService.getPassengersForFlight(flightId));
    }

    @DeleteMapping("/{flightId}/passengers/{userId}")
    public ResponseEntity<String> removePassenger(
            @PathVariable Long flightId,
            @PathVariable Long userId
    ) {
        return flightService.removeUserFromFlight(flightId, userId);
    }

    @PutMapping("/{flightId}/start")
    public ResponseEntity<String> startFlight(@PathVariable Long flightId) {
        return flightService.startFlight(flightId);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Flight>> getAllStartedFlights() {
        return ResponseEntity.ok(flightService.getAllStartedFlights());
    }
}