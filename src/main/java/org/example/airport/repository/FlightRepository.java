package org.example.airport.repository;

import org.example.airport.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByStartedFalseAndAvailableSeatsGreaterThan(int seats);

    @Query("SELECT f FROM Flight f LEFT JOIN FETCH f.passengers WHERE f.id = :id")
    Optional<Flight> findByIdWithPassengers(@Param("id") Long id);
}