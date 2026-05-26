package com.flights.project.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flights.project.entities.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    
    
    Optional<Passenger> findByEmail(String email);
}