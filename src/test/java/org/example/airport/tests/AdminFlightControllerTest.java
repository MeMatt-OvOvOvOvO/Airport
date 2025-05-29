package org.example.airport.controllers;

import org.example.airport.dto.FlightReportDto;
import org.example.airport.entity.Flight;
import org.example.airport.entity.User;
import org.example.airport.repository.FlightRepository;
import org.example.airport.repository.UserRepository;
import org.example.airport.services.FlightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminFlightControllerTest {

    @Mock
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminFlightController controller;

    @Test
    void shouldCreateFlightWithDefaultDepartureTime() {
        Flight flight = new Flight();
        when(flightService.addFlight(any())).thenReturn(flight);

        ResponseEntity<Flight> response = controller.create(new Flight());

        assertEquals(200, response.getStatusCodeValue());
        verify(flightService).addFlight(any());
    }

    @Test
    void shouldGetPassengersForFlight() {
        List<User> passengers = List.of(new User());
        when(flightService.getPassengersForFlight(1L)).thenReturn(passengers);

        ResponseEntity<List<User>> response = controller.getPassengers(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldRemovePassengerFromFlight() {
        when(flightService.removeUserFromFlight(1L, 2L)).thenReturn(ResponseEntity.ok("removed"));

        ResponseEntity<String> response = controller.removePassenger(1L, 2L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("removed", response.getBody());
    }

    @Test
    void shouldStartFlight() {
        when(flightService.startFlight(1L)).thenReturn(ResponseEntity.ok("started"));

        ResponseEntity<String> response = controller.startFlight(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("started", response.getBody());
    }

    @Test
    void shouldGetStartedFlights() {
        List<Flight> flights = List.of(new Flight());
        when(flightService.getAllStartedFlights()).thenReturn(flights);

        ResponseEntity<List<Flight>> response = controller.getAllStartedFlights();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldGetFlightReports() {
        List<FlightReportDto> reports = List.of(new FlightReportDto());
        when(flightService.generateFlightReports()).thenReturn(reports);

        ResponseEntity<List<FlightReportDto>> response = controller.getReport();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldClearAllData() {
        ResponseEntity<String> response = controller.clearAll();

        verify(flightRepository).deleteAll();
        verify(userRepository).deleteAll();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Wszystkie dane usunięte.", response.getBody());
    }
}
