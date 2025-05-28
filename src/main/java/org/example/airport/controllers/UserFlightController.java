package org.example.airport.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.airport.dto.BaggageUpdateRequest;
import org.example.airport.dto.FlightDto;
import org.example.airport.entity.Flight;
import org.example.airport.repository.UserRepository;
import org.example.airport.services.FlightService;
import org.example.airport.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user/flights")
@RequiredArgsConstructor
@Tag(name = "Loty użytkownika", description = "Operacje dostępne dla użytkowników (rola USER)")
public class UserFlightController {

    private final FlightService flightService;
    private final UserRepository userRepository;

    @Operation(summary = "Zapisz się na lot", description = "Użytkownik zapisuje się na dostępny lot")
    @PutMapping("/{flightId}/register")
    public ResponseEntity<String> registerToFlight(
            @PathVariable Long flightId,
            @AuthenticationPrincipal User user
    ) {
        return flightService.registerToFlight(flightId, user);
    }

    @Operation(
            summary = "Zaktualizuj wagę bagażu",
            description = "Pozwala zalogowanemu użytkownikowi zaktualizować wagę swojego bagażu przed zapisaniem się na lot."
    )
    @PutMapping("/baggage")
    public ResponseEntity<String> updateBaggage(
            @RequestBody BaggageUpdateRequest request,
            @AuthenticationPrincipal User user
    ) {
        user.setBaggageWeight(request.getBaggageWeight());
        userRepository.save(user);
        return ResponseEntity.ok("Waga bagażu zaktualizowana.");
    }

    @Operation(
            summary = "Pobierz historię lotów",
            description = "Zwraca listę lotów zakończonych, w których uczestniczył zalogowany użytkownik."
    )
    @GetMapping("/history")
    public ResponseEntity<List<Flight>> getMyFlightHistory(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(flightService.getUserFlightHistory(user));
    }

    @Operation(
            summary = "Pobierz swoje loty",
            description = "Zwraca listę lotów, na które aktualnie zapisany jest zalogowany użytkownik."
    )
    @GetMapping("/upcoming")
    public ResponseEntity<List<FlightDto>> getMyUpcomingFlights(@AuthenticationPrincipal User user) {
        List<FlightDto> upcoming = user.getFlights().stream()
                .filter(f -> f.getDepartureTime().isAfter(LocalDateTime.now()))
                .map(f -> new FlightDto(f.getId(), f.getDestination(), f.getDepartureTime()))
                .toList();
        return ResponseEntity.ok(upcoming);
    }
}