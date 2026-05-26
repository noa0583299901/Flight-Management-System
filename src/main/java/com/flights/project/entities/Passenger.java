package com.flights.project.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password; 
    @Column(name = "passenger_role") 
    private String role;    

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
