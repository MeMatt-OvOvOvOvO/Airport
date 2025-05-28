package org.example.airport.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.airport.entity.Flight;
import org.example.airport.services.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
@Tag(name = "Loty", description = "Informacje o dostępnych lotach")
public class PublicFlightController {

    private final FlightService flightService;

    @Operation(
            summary = "Pobierz dostępne loty",
            description = "Zwraca listę wszystkich lotów, które jeszcze nie wystartowały i mają wolne miejsca. Dostępne dla każdego użytkownika."
    )
    @GetMapping("/available")
    public ResponseEntity<List<Flight>> getAvailableFlights() {
        return ResponseEntity.ok(flightService.getAvailableFlights());
    }
}