package org.example.airport.controllers;

import lombok.RequiredArgsConstructor;
import org.example.airport.entity.Flight;
import org.example.airport.services.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class PublicFlightController {

    private final FlightService flightService;

    @GetMapping("/available")
    public ResponseEntity<List<Flight>> getAvailableFlights() {
        return ResponseEntity.ok(flightService.getAvailableFlights());
    }
}