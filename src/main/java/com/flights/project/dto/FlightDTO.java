package com.flights.project.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double basePrice;
    
   
    private int availableSeats; 
    
    private List<String> passengerNames; 
}