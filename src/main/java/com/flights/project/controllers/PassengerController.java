package com.flights.project.controllers;

import com.flights.project.dto.LoginRequestDTO;
import com.flights.project.dto.LoginResponseDTO;
import com.flights.project.dto.PassengerDTO;
import com.flights.project.entities.Passenger;
import com.flights.project.services.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping("/register")
    public ResponseEntity<Passenger> register(@RequestBody Passenger passenger) {
        return ResponseEntity.ok(passengerService.registerPassenger(passenger));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(passengerService.login(loginRequest));
    }
 @GetMapping("/{id}")
public ResponseEntity<PassengerDTO> getPassengerById(@PathVariable Long id) {
    PassengerDTO passenger = passengerService.getPassengerById(id);
    return ResponseEntity.ok(passenger);
}
}
