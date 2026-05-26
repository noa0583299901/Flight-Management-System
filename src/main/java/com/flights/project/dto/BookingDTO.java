package com.flights.project.dto;
import com.flights.project.entities.ClassType;

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
    private ClassType classType;
}