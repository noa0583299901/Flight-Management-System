package com.flights.project.dto;
import lombok.Data;

@Data
public class PassengerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}