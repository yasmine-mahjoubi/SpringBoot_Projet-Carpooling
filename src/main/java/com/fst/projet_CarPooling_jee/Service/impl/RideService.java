package com.fst.projet_CarPooling_jee.Service.impl;

import com.fst.projet_CarPooling_jee.Entity.Ride;
import com.fst.projet_CarPooling_jee.Repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideService {
    @Autowired
    private RideRepository rideRepository;

    // Get all rides
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    // Save a ride to the database
    public void saveRide(Ride ride) {
        this.rideRepository.save(ride);
    }

    // Find rides based on search criteria
    public List<Ride> findRides(String startLocation, String endLocation, Date departureDate, Integer nbPassengers) {
        // Return rides filtered based on given parameters
        if (departureDate != null && nbPassengers != null) {
            return rideRepository.findByStartLocationContainingAndEndLocationContainingAndDepartureDateAndAvailableSeatsGreaterThanEqual(
                    startLocation, endLocation, departureDate, nbPassengers);
        } else if (departureDate != null) {
            return rideRepository.findByStartLocationContainingAndEndLocationContainingAndDepartureDate(
                    startLocation, endLocation, departureDate);
        } else if (nbPassengers != null) {
            return rideRepository.findByStartLocationContainingAndEndLocationContainingAndAvailableSeatsGreaterThanEqual(
                    startLocation, endLocation, nbPassengers);
        } else {
            return rideRepository.findByStartLocationContainingAndEndLocationContaining(
                    startLocation, endLocation);
        }
    }

    public List<Ride> findRidesByDriverId(Long driverId) {
        return rideRepository.findByDriverId(driverId);
    }

    //implemenatation de methode de pagination
    /*public Page<Ride> findPaginated(int pageNo, int pageSize) {
        //spring data jpa provides Pageable(interface) and PageRequest(class that implements Pageable)
        //pageNo - 1 to start with 0
        Pageable pageable = PageRequest.of(pageNo - 1 , pageSize);
        return this.rideRepository.findAll(pageable);
    }*/



    /*public Page<Ride> findPaginatedByDriverId(Long driverId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1 , pageSize); // Spring Data JPA commence à la page 0
        return rideRepository.findByDriverId(driverId,pageable);// Appel au repository
    }*/

    public void updateAvailableSeats(Long rideId, int reservedPlaces) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        int updatedSeats = ride.getAvailableSeats() - reservedPlaces;
        if (updatedSeats < 0) {
            throw new RuntimeException("Not enough available seats");
        }
        ride.setAvailableSeats(updatedSeats);
        rideRepository.save(ride);
    }

    public Ride getRideById(Long RideID){
        return rideRepository.findRideById(RideID);
    }

}
