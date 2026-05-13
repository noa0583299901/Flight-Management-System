package com.flights.project.services;
import com.flights.project.dto.BookingDTO; 
import com.flights.project.entities.Flight;
import com.flights.project.entities.Passenger;
import com.flights.project.entities.Booking;
import com.flights.project.Repositories.FlightRepository; 
import com.flights.project.Repositories.PassengerRepository;
import com.flights.project.Repositories.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @MockBean
    private FlightRepository flightRepository;

    @MockBean
    private PassengerRepository passengerRepository;

    @MockBean
    private BookingRepository bookingRepository;

    @Test
    public void testCreateBookingFailsWhenNoSeats() {
        Flight fullFlight = new Flight();
        fullFlight.setId(1L);
        fullFlight.setAvailableSeats(0);

        BookingDTO bookingRequest = new BookingDTO();
        bookingRequest.setFlightId(1L);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(fullFlight));

        assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });
    }

    @Test
    public void testCreateBookingSuccess() {
        Flight flight = new Flight();
        flight.setId(2L);
        flight.setAvailableSeats(10);
        flight.setBasePrice(100.0);

        Passenger passenger = new Passenger();
        passenger.setId(1L);

        BookingDTO request = new BookingDTO();
        request.setFlightId(2L);
        request.setPassengerId(1L);
        request.setSeatNumber("12A");

        when(flightRepository.findById(2L)).thenReturn(Optional.of(flight));
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        BookingDTO result = bookingService.createBooking(request);

        assertNotNull(result);
        assertEquals(9, flight.getAvailableSeats()); 
    }
}
