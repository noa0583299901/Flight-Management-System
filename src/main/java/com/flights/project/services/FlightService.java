package com.flights.project.services;

import com.flights.project.dto.FlightDTO;
import com.flights.project.entities.Flight;
import com.flights.project.Repositories.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FlightDTO> getAvailableFlightsTo(String destination) {
        return flightRepository.findAll().stream()
                .filter(f -> f.getDestination().equalsIgnoreCase(destination))
                .filter(f -> f.getAvailableSeats() > 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
       public List<FlightDTO> getFlightsWithAvailableSeats() {
       return flightRepository.findAll().stream()
            .filter(f -> f.getAvailableSeats() > 0)
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    public FlightDTO convertToDTO(Flight flight) {
        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setOrigin(flight.getOrigin());
        dto.setDestination(flight.getDestination());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setBasePrice(flight.getBasePrice());
        dto.setAvailableSeats(flight.getAvailableSeats());

        if (flight.getBookings() != null) {
            dto.setPassengerNames(flight.getBookings().stream()
                    .map(b -> b.getPassenger().getFirstName() + " " + b.getPassenger().getLastName())
                    .collect(Collectors.toList()));
        }
        return dto;
    }
   public FlightDTO saveFlight(FlightDTO flightDTO) {
     Flight flight = new Flight();
    flight.setId(flightDTO.getId());
    flight.setFlightNumber(flightDTO.getFlightNumber());
    flight.setOrigin(flightDTO.getOrigin());
    flight.setDestination(flightDTO.getDestination());
    flight.setDepartureTime(flightDTO.getDepartureTime());
    flight.setBasePrice(flightDTO.getBasePrice());
    flight.setAvailableSeats(flightDTO.getAvailableSeats());
Flight savedFlight = flightRepository.save(flight);
  return convertToDTO(savedFlight);
}
}
