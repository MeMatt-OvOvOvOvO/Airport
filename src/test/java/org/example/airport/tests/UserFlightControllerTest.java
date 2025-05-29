package org.example.airport.tests;

import org.example.airport.controllers.UserFlightController;
import org.example.airport.dto.BaggageUpdateRequest;
import org.example.airport.dto.FlightDto;
import org.example.airport.entity.Flight;
import org.example.airport.entity.User;
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
public class UserFlightControllerTest {

    @Mock
    private FlightService flightService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserFlightController controller;

    @Test
    public void shouldRegisterToFlight() {
        User user = new User();
        when(flightService.registerToFlight(1L, user)).thenReturn(ResponseEntity.ok("Zapisano"));

        ResponseEntity<String> response = controller.registerToFlight(1L, user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Zapisano", response.getBody());
    }

    @Test
    public void shouldUpdateBaggageWeight() {
        User user = new User();
        BaggageUpdateRequest request = new BaggageUpdateRequest();
        request.setBaggageWeight(20.0);

        ResponseEntity<String> response = controller.updateBaggage(request, user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(20.0, user.getBaggageWeight());
        assertEquals("Waga bagażu zaktualizowana.", response.getBody());
        verify(userRepository).save(user);
    }

    @Test
    public void shouldReturnFlightHistory() {
        User user = new User();
        List<Flight> history = List.of(new Flight(), new Flight());
        when(flightService.getUserFlightHistory(user)).thenReturn(history);

        ResponseEntity<List<Flight>> response = controller.getMyFlightHistory(user);

        assertEquals(2, response.getBody().size());
    }

    @Test
    public void shouldReturnUpcomingFlights() {
        User user = new User();

        Flight flight1 = new Flight();
        flight1.setId(1L);
        flight1.setDestination("Berlin");
        flight1.setDepartureTime(LocalDateTime.now().plusDays(1));

        Flight flight2 = new Flight();
        flight2.setId(2L);
        flight2.setDestination("Paris");
        flight2.setDepartureTime(LocalDateTime.now().minusDays(1));

        user.setFlights(List.of(flight1, flight2));

        ResponseEntity<List<FlightDto>> response = controller.getMyUpcomingFlights(user);

        assertEquals(1, response.getBody().size());
        assertEquals("Berlin", response.getBody().get(0).destination());
    }
}
