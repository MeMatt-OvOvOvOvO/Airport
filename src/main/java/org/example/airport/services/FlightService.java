package org.example.airport.services;

import lombok.RequiredArgsConstructor;
import org.example.airport.entity.Flight;
import org.example.airport.repository.FlightRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }
}