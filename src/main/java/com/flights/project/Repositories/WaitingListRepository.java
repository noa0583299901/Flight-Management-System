package com.flights.project.Repositories;

import com.flights.project.entities.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {
    
    
    List<WaitingList> findByFlightIdOrderByRequestTimeAsc(Long flightId);

    void deleteByFlightIdAndPassengerId(Long flightId, Long passengerId);
}
