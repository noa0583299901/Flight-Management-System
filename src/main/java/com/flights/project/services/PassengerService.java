package com.flights.project.services;

import com.flights.project.dto.LoginRequestDTO;
import com.flights.project.dto.LoginResponseDTO;
import com.flights.project.dto.PassengerDTO;
import com.flights.project.entities.Passenger;
import com.flights.project.Repositories.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt; 
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

   
    public Passenger registerPassenger(Passenger passenger) {
        if (passengerRepository.findByEmail(passenger.getEmail()).isPresent()) {
            throw new RuntimeException("כתובת האימייל הזו כבר קיימת במערכת!");
        }
        
      
        
        String hashedPassword = BCrypt.hashpw(passenger.getPassword(), BCrypt.gensalt());
        passenger.setPassword(hashedPassword); 
        
        passenger.setRole("PASSENGER"); 
        return passengerRepository.save(passenger);
    }

    
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Passenger passenger = passengerRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("שם משתמש או סיסמה שגויים"));

        
        
        if (!BCrypt.checkpw(loginRequest.getPassword(), passenger.getPassword())) {
            throw new RuntimeException("שם משתמש או סיסמה שגויים");
        }

        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(passenger.getId());
        response.setFirstName(passenger.getFirstName());
        response.setLastName(passenger.getLastName());
        response.setEmail(passenger.getEmail());
        response.setRole(passenger.getRole());
        return response;
    }
    public PassengerDTO getPassengerById(Long id) {
    
    Passenger passenger = passengerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + id));
            PassengerDTO dto = new PassengerDTO();
    dto.setId(passenger.getId());
    dto.setFirstName(passenger.getFirstName());
    dto.setLastName(passenger.getLastName());
    dto.setEmail(passenger.getEmail());
   dto.setRole(passenger.getRole());
    
    return dto;
}
}