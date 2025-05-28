package org.example.airport.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.airport.dto.FlightReportDto;
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
@Tag(name = "Loty administratora", description = "Operacje dostępne dla administratora (rola ADMIN)")
public class AdminFlightController {

    private final FlightService flightService;

    @Operation(
            summary = "Utwórz nowy lot",
            description = "Pozwala administratorowi dodać nowy lot do systemu."
    )
    @PostMapping
    public ResponseEntity<Flight> create(@RequestBody Flight flight) {
        return ResponseEntity.ok(flightService.addFlight(flight));
    }

    @Operation(
            summary = "Pobierz pasażerów lotu",
            description = "Zwraca listę użytkowników zapisanych na dany lot."
    )
    @GetMapping("/{flightId}/passengers")
    public ResponseEntity<List<User>> getPassengers(@PathVariable Long flightId) {
        return ResponseEntity.ok(flightService.getPassengersForFlight(flightId));
    }

    @Operation(
            summary = "Usuń pasażera z lotu",
            description = "Pozwala administratorowi usunąć użytkownika z konkretnego lotu."
    )
    @DeleteMapping("/{flightId}/passengers/{userId}")
    public ResponseEntity<String> removePassenger(
            @PathVariable Long flightId,
            @PathVariable Long userId
    ) {
        return flightService.removeUserFromFlight(flightId, userId);
    }

    @Operation(
            summary = "Rozpocznij lot",
            description = "Oznacza lot jako rozpoczęty. Nie można już zapisywać nowych pasażerów."
    )
    @PutMapping("/{flightId}/start")
    public ResponseEntity<String> startFlight(@PathVariable Long flightId) {
        return flightService.startFlight(flightId);
    }

    @Operation(
            summary = "Pobierz historię lotów",
            description = "Zwraca wszystkie loty, które zostały już rozpoczęte."
    )
    @GetMapping("/history")
    public ResponseEntity<List<Flight>> getAllStartedFlights() {
        return ResponseEntity.ok(flightService.getAllStartedFlights());
    }

    @Operation(
            summary = "Wygeneruj raport lotów",
            description = "Zwraca statystyki dotyczące lotów, liczby pasażerów i średniego obciążenia bagażowego."
    )
    @GetMapping("/report")
    public ResponseEntity<List<FlightReportDto>> getReport() {
        return ResponseEntity.ok(flightService.generateFlightReports());
    }
}