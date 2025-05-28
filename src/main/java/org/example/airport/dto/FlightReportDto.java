package org.example.airport.dto;

import lombok.Data;

import java.util.List;

@Data
public class FlightReportDto {
    private Long flightId;
    private String destination;
    private boolean started;
    private int seatLimit;
    private double baggageLimit;
    private List<PassengerDto> passengers;

    @Data
    public static class PassengerDto {
        private Long id;
        private String username;
        private double baggageWeight;
    }
}