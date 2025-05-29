package org.example.airport.tests;

import org.example.airport.dto.FlightReportDto;
import org.example.airport.entity.Flight;
import org.example.airport.entity.User;
import org.example.airport.repository.FlightRepository;
import org.example.airport.repository.UserRepository;
import org.example.airport.services.FlightService;
import org.example.airport.strategy.BaggageCheckStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private BaggageCheckStrategy baggageCheckStrategy;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    public void shouldRegisterUserToFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(5);
        flight.setStarted(false);
        flight.setPassengers(new ArrayList<>());

        User user = new User();
        user.setId(2L);
        user.setBaggageWeight(15.0);

        when(flightRepository.findByIdWithPassengers(1L)).thenReturn(Optional.of(flight));
        when(baggageCheckStrategy.isAllowed(anyDouble(), anyDouble())).thenReturn(true);
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        ResponseEntity<String> response = flightService.registerToFlight(1L, user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Zapisano na lot.", response.getBody());
        verify(flightRepository).save(flight);
    }

    @Test
    public void shouldRejectUserWithTooHeavyBaggage() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(5);
        flight.setStarted(false);
        flight.setPassengers(new ArrayList<>());
        flight.setBaggageLimit(10.0);

        User user = new User();
        user.setId(2L);
        user.setBaggageWeight(15.0);

        when(flightRepository.findByIdWithPassengers(1L)).thenReturn(Optional.of(flight));
        when(baggageCheckStrategy.isAllowed(15.0, 10.0)).thenReturn(false);

        ResponseEntity<String> response = flightService.registerToFlight(1L, user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Twój bagaż przekracza limit i nie możesz zapisać się na lot.", response.getBody());
    }

    @Test
    public void shouldRejectDuplicateRegistration() {
        User user = new User();
        user.setId(1L);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(5);
        flight.setStarted(false);
        List<User> passengers = new ArrayList<>();
        passengers.add(user);
        flight.setPassengers(passengers);

        when(flightRepository.findByIdWithPassengers(1L)).thenReturn(Optional.of(flight));
        when(baggageCheckStrategy.isAllowed(anyDouble(), anyDouble())).thenReturn(true);

        ResponseEntity<String> response = flightService.registerToFlight(1L, user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Jesteś już zapisany na ten lot.", response.getBody());
    }

    @Test
    public void shouldReturnAvailableFlights() {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());
        when(flightRepository.findByStartedFalseAndAvailableSeatsGreaterThan(0)).thenReturn(flights);

        List<Flight> result = flightService.getAvailableFlights();

        assertEquals(1, result.size());
    }

    @Test
    public void shouldStartFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setStarted(false);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        ResponseEntity<String> result = flightService.startFlight(1L);

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(flight.isStarted());
        assertEquals("Lot wystartował – użytkownicy zostali powiadomieni.", result.getBody());
    }

    @Test
    public void shouldReturnStartedFlights() {
        Flight started = new Flight();
        started.setStarted(true);
        Flight notStarted = new Flight();
        notStarted.setStarted(false);

        when(flightRepository.findAll()).thenReturn(List.of(started, notStarted));

        List<Flight> result = flightService.getAllStartedFlights();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isStarted());
    }

    @Test
    public void shouldGenerateFlightReport() {
        User user = new User();
        user.setBaggageWeight(20.0);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setDestination("Paris");
        flight.setDepartureTime(LocalDateTime.now());
        flight.setPassengers(List.of(user));

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<FlightReportDto> reports = flightService.generateFlightReports();

        assertEquals(1, reports.size());
        assertEquals(1, reports.get(0).getPassengerCount());
        assertEquals(20.0, reports.get(0).getTotalBaggageWeight());
    }

    @Test
    public void shouldRemoveUserFromFlight() {
        User user = new User();
        user.setId(1L);
        user.setFlights(new ArrayList<>());

        Flight flight = new Flight();
        flight.setId(2L);
        flight.setPassengers(new ArrayList<>());
        flight.getPassengers().add(user);

        user.getFlights().add(flight);

        when(flightRepository.findById(2L)).thenReturn(Optional.of(flight));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = flightService.removeUserFromFlight(2L, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pasażer został usunięty z lotu.", response.getBody());
        assertFalse(flight.getPassengers().contains(user));
        assertFalse(user.getFlights().contains(flight));
    }

    @Test
    public void shouldReturnErrorWhenRemovingUserFromNonexistentFlight() {
        when(flightRepository.findById(99L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = flightService.removeUserFromFlight(99L, 1L);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Lot lub użytkownik nie istnieje.", response.getBody());
    }

    @Test
    public void shouldReturnErrorWhenUserNotInFlight() {
        User user = new User();
        user.setId(1L);

        Flight flight = new Flight();
        flight.setId(2L);
        flight.setPassengers(new ArrayList<>());

        when(flightRepository.findById(2L)).thenReturn(Optional.of(flight));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = flightService.removeUserFromFlight(2L, 1L);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Użytkownik nie jest zapisany na ten lot.", response.getBody());
    }

    @Test
    public void shouldReturnPassengersForFlight() {
        User user1 = new User();
        User user2 = new User();

        Flight flight = new Flight();
        flight.setPassengers(List.of(user1, user2));

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        List<User> passengers = flightService.getPassengersForFlight(1L);

        assertEquals(2, passengers.size());
    }

    @Test
    public void shouldReturnUserFlightHistory() {
        Flight started = new Flight();
        started.setStarted(true);
        Flight notStarted = new Flight();
        notStarted.setStarted(false);

        User user = new User();
        user.setFlights(List.of(started, notStarted));

        List<Flight> result = flightService.getUserFlightHistory(user);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isStarted());
    }

    @Test
    public void shouldAddFlight() {
        Flight flight = new Flight();
        when(flightRepository.save(flight)).thenReturn(flight);

        Flight result = flightService.addFlight(flight);

        assertEquals(flight, result);
        verify(flightRepository).save(flight);
    }

    @Test
    public void shouldReturnAllFlights() {
        Flight f1 = new Flight();
        Flight f2 = new Flight();

        when(flightRepository.findAll()).thenReturn(List.of(f1, f2));

        List<Flight> flights = flightService.getAllFlights();

        assertEquals(2, flights.size());
    }

    @Test
    public void shouldRejectRegistrationIfFlightAlreadyStarted() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setStarted(true);
        flight.setPassengers(new ArrayList<>());

        User user = new User();
        user.setId(2L);

        when(flightRepository.findByIdWithPassengers(1L)).thenReturn(Optional.of(flight));

        ResponseEntity<String> response = flightService.registerToFlight(1L, user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Lot już wystartował.", response.getBody());
    }

    @Test
    public void shouldRejectRegistrationWhenNoSeatsAvailable() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setStarted(false);
        flight.setAvailableSeats(0);
        flight.setPassengers(new ArrayList<>());

        User user = new User();
        user.setId(3L);
        user.setBaggageWeight(10.0);

        when(flightRepository.findByIdWithPassengers(1L)).thenReturn(Optional.of(flight));
        when(baggageCheckStrategy.isAllowed(anyDouble(), anyDouble())).thenReturn(true);

        ResponseEntity<String> response = flightService.registerToFlight(1L, user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Brak dostępnych miejsc.", response.getBody());
    }
}