package org.example.airport.dto;

import java.time.LocalDateTime;

public record FlightDto(
        Long id,
        String destination,
        LocalDateTime departureTime
) {}