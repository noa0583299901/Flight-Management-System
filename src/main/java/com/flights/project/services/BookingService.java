package com.flights.project.services;

import com.flights.project.dto.BookingDTO;
import com.flights.project.entities.Booking;
import com.flights.project.entities.Flight;
import com.flights.project.entities.Passenger;
import com.flights.project.Repositories.BookingRepository;
import com.flights.project.Repositories.FlightRepository;
import com.flights.project.Repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
     
        Flight flight = flightRepository.findById(bookingDTO.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available on this flight!");
        }

       
        Passenger passenger = passengerRepository.findById(bookingDTO.getPassengerId())
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

    
        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        booking.setSeatNumber(bookingDTO.getSeatNumber());
        booking.setFinalPrice(flight.getBasePrice());

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

       
        Booking savedBooking = bookingRepository.save(booking);
        return convertToDTO(savedBooking);
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

public Booking convertToEntity(BookingDTO dto) {
    Booking booking = new Booking();

    Flight flight = flightRepository.findById(dto.getFlightId())
            .orElseThrow(() -> new RuntimeException("Flight not found"));
    
    booking.setFlight(flight);
    booking.setSeatNumber(dto.getSeatNumber());
    booking.setFinalPrice(flight.getBasePrice());
    return booking;
}

private double calculatePrice(Flight flight) {
    if (flight.getDepartureTime().isAfter(LocalDateTime.now().plusMonths(1))) {
        return flight.getBasePrice() * 0.9; 
    }
    return flight.getBasePrice();
}
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setFlightId(booking.getFlight().getId());
        dto.setFlightNumber(booking.getFlight().getFlightNumber());
        dto.setPassengerId(booking.getPassenger().getId());
        dto.setPassengerFullName(booking.getPassenger().getFirstName() + " " + booking.getPassenger().getLastName());
        dto.setSeatNumber(booking.getSeatNumber());
        dto.setFinalPrice(booking.getFinalPrice());
        return dto;
    }
}
