package org.example.airport.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlightReportDto {
    private Long flightId;
    private String destination;
    private boolean started;
    private int seatLimit;
    private double baggageLimit;
    private List<PassengerDto> passengers;
    private LocalDateTime departureTime;
    private int passengerCount;

    @Schema(description = "Suma wagi bagaży wszystkich pasażerów")
    private double totalBaggageWeight;

    @Data
    public static class PassengerDto {
        private Long id;
        private String username;
        private double baggageWeight;
    }
}