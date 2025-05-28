package org.example.airport.services;

import lombok.RequiredArgsConstructor;
import org.example.airport.entity.Flight;
import org.example.airport.entity.User;
import org.example.airport.repository.FlightRepository;
import org.example.airport.repository.UserRepository;
import org.example.airport.strategy.BaggageCheckStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final BaggageCheckStrategy baggageCheckStrategy;
    private final UserRepository userRepository;

    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public ResponseEntity<String> registerToFlight(Long flightId, User user) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if (flight.isStarted()) {
            return ResponseEntity.badRequest().body("Lot już wystartował.");
        }

        if (!baggageCheckStrategy.isAllowed(user.getBaggageWeight(), flight.getBaggageLimit())) {
            return ResponseEntity.badRequest().body("Twój bagaż przekracza limit i nie możesz zapisać się na lot.");
        }

        if (flight.getPassengers().contains(user)) {
            return ResponseEntity.badRequest().body("Jesteś już zapisany na ten lot.");
        }

        if (flight.getAvailableSeats() <= 0) {
            return ResponseEntity.badRequest().body("Brak dostępnych miejsc.");
        }

        flight.getPassengers().add(user);
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);
        return ResponseEntity.ok("Zapisano na lot.");
    }

    public List<Flight> getAvailableFlights() {
        return flightRepository.findByStartedFalseAndAvailableSeatsGreaterThan(0);
    }

    public List<User> getPassengersForFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Lot nie istnieje"));
        return flight.getPassengers();
    }

    public ResponseEntity<String> removeUserFromFlight(Long flightId, Long userId) {
        Flight flight = flightRepository.findById(flightId)
                .orElse(null);
        User user = userRepository.findById(userId)
                .orElse(null);

        if (flight == null || user == null) {
            return ResponseEntity.badRequest().body("Lot lub użytkownik nie istnieje.");
        }

        if (!flight.getPassengers().contains(user)) {
            return ResponseEntity.badRequest().body("Użytkownik nie jest zapisany na ten lot.");
        }

        flight.getPassengers().remove(user);
        user.getFlights().remove(flight);

        flightRepository.save(flight);
        userRepository.save(user);

        return ResponseEntity.ok("Pasażer został usunięty z lotu.");
    }

    public ResponseEntity<String> startFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElse(null);

        if (flight == null) {
            return ResponseEntity.badRequest().body("Lot nie istnieje.");
        }

        if (flight.isStarted()) {
            return ResponseEntity.badRequest().body("Lot już został wystartowany.");
        }

        flight.setStarted(true);
        flightRepository.save(flight);

        return ResponseEntity.ok("Lot wystartował – użytkownicy zostali powiadomieni.");
    }
}