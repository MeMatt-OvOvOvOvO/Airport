package org.example.airport.controllers;

import lombok.RequiredArgsConstructor;
import org.example.airport.entity.Flight;
import org.example.airport.services.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<Flight> create(@RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.addFlight(flight));
    }
}