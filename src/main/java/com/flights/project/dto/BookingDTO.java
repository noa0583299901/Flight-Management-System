package com.flights.project.dto;
import lombok.Data;

@Data
public class BookingDTO {
    private Long id;
    private Long flightId;
    private String flightNumber;
    private Long passengerId;
    private String passengerFullName;
    private String seatNumber;
    private double finalPrice;
}