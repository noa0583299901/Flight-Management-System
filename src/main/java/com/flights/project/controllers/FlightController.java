package com.flights.project.controllers;
import com.flights.project.dto.FlightDTO;
import com.flights.project.services.FlightService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public List<FlightDTO> getAllFlights() {
        return flightService.getAllFlights();
    }

    @GetMapping("/available")
    public List<FlightDTO> getAvailableFlights(@RequestParam String destination) {
        return flightService.getAvailableFlightsTo(destination);
    }
 @PostMapping
public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightDTO flightDTO) {
    FlightDTO savedFlight = flightService.saveFlight(flightDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
}
}