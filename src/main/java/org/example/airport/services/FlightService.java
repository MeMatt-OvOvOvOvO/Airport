package org.example.airport.services;

import lombok.RequiredArgsConstructor;
import org.example.airport.entity.Flight;
import org.example.airport.entity.User;
import org.example.airport.repository.FlightRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public ResponseEntity<String> registerToFlight(Long flightId, User user) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if (flight.isStarted()) {
            return ResponseEntity.badRequest().body("Lot już wystartował.");
        }

        if (user.getBaggageWeight() > flight.getBaggageLimit()) {
            return ResponseEntity.badRequest().body("Twój bagaż jest za ciężki.");
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
}