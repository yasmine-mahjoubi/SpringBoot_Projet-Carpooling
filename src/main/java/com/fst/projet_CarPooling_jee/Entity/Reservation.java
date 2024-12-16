package com.fst.projet_CarPooling_jee.Entity;

import com.fst.projet_CarPooling_jee.Entity.enums.ReservationStatus;
import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer NbReservedPlaces;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // ENUM : PENDING, CONFIRMED, CANCELED


    //@JoinColumn permet de spécifier la colonne dans la table actuelle qui contient la clé étrangère pointant vers l'entité associée (ici User).
    @ManyToOne
   // nullable = false : Cela signifie que la colonne user_id ne peut pas être nulle. Autrement dit, chaque réservation doit être associée à un utilisateur, et cette association est obligatoire.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // L'utilisateur (passager) qui réserve
    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;  // Référence vers le trajet réservé


    // getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbReservedPlaces() {
        return NbReservedPlaces;
    }

    public void setNbReservedPlaces(Integer nbReservedPlaces) {
        NbReservedPlaces = nbReservedPlaces;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}

