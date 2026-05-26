package com.flights.project.services;

import com.flights.project.dto.BookingDTO;
import com.flights.project.entities.*;
import com.flights.project.Repositories.*;
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
    private final WaitingListRepository waitingListRepository;
    private final EmailService emailService;

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Flight flight = flightRepository.findById(bookingDTO.getFlightId())
                .orElseThrow(() -> new RuntimeException("הטיסה המבוקשת לא נמצאה במערכת"));

        Passenger passenger = passengerRepository.findById(bookingDTO.getPassengerId())
                .orElseThrow(() -> new RuntimeException("הנוסע לא נמצא במערכת"));

        
        if (flight.getAvailableSeats() <= 0) {
            WaitingList wl = new WaitingList();
            wl.setFlight(flight);
            wl.setPassenger(passenger);
            wl.setRequestTime(LocalDateTime.now());
            waitingListRepository.save(wl);
            throw new RuntimeException("הטיסה מלאה! נרשמת אוטומטית לרשימת ההמתנה. תקבל מייל ברגע שיתפנה מקום.");
        }

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        booking.setSeatNumber(bookingDTO.getSeatNumber());
        booking.setClassType(bookingDTO.getClassType());

        double calculatedPrice = flight.getBasePrice();
        if (bookingDTO.getClassType() == ClassType.BUSINESS) calculatedPrice *= 1.5;
        if (bookingDTO.getClassType() == ClassType.FIRST) calculatedPrice *= 2.5;    
        booking.setFinalPrice(calculatedPrice);

        
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

    
        waitingListRepository.deleteByFlightIdAndPassengerId(flight.getId(), passenger.getId());

        Booking savedBooking = bookingRepository.save(booking);

        
        emailService.sendBookingConfirmation(
                passenger.getEmail(), 
                passenger.getFirstName() + " " + passenger.getLastName(), 
                flight.getFlightNumber(), 
                booking.getSeatNumber()
        );

        return convertToDTO(savedBooking);
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("ההזמנה לא נמצאה"));

        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);

        bookingRepository.delete(booking);

        
        List<WaitingList> waitingPassengers = waitingListRepository.findByFlightIdOrderByRequestTimeAsc(flight.getId());
        for (WaitingList wl : waitingPassengers) {
            emailService.sendWaitingListNotification(wl.getPassenger().getEmail(), flight.getFlightNumber());
        }
    }

    public List<BookingDTO> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getPassenger().getId().equals(passengerId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
        dto.setClassType(booking.getClassType());
        return dto;
    }
}
