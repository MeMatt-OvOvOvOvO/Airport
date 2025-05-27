package org.example.airport.repository;

import org.example.airport.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByStartedFalseAndAvailableSeatsGreaterThan(int seats);
}