package com.fst.projet_CarPooling_jee.Service.impl;

import com.fst.projet_CarPooling_jee.Entity.Reservation;
import com.fst.projet_CarPooling_jee.Entity.Ride;
import com.fst.projet_CarPooling_jee.Entity.User;
import com.fst.projet_CarPooling_jee.Entity.enums.ReservationStatus;
import com.fst.projet_CarPooling_jee.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RideService rideService;

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
    public void saveReservation(Reservation reservation) {
        this.reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));
    }


    public void deleteReservation(Long id) {
        Reservation reservation = getReservationById(id);
        reservationRepository.delete(reservation);
    }


    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = getReservationById(id);
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    public void saveReservation(Long rideId, Long userId, int reservedPlaces) {
        rideService.updateAvailableSeats(rideId, reservedPlaces);

        Reservation reservation = new Reservation();
        Ride ride = rideService.getRideById(rideId);
        User user = new User();
        user.setId(userId);

        reservation.setRide(ride);
        reservation.setUser(user);
        reservation.setNbReservedPlaces(reservedPlaces);

        reservationRepository.save(reservation);
    }
    // Récupérer toutes les réservations d'un utilisateur spécifique
    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

}