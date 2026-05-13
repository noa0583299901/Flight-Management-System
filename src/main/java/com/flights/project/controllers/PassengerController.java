package com.flights.project.controllers;

import com.flights.project.dto.PassengerDTO;
import com.flights.project.services.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    public List<PassengerDTO> getAllPassengers() {
        return passengerService.getAllPassengers();
    }
}
