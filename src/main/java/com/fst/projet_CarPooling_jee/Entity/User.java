package com.fst.projet_CarPooling_jee.Entity;



//import Entity.enums.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

    private Double averageRating; //Moyenne des notes que l'utilisateur a reçues (pour les conducteurs et passagers).

    // Relations

    //pas obligatoire
    //Un User peut avoir plusieurs Ride en tant que conducteur.
    //table ride
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ride> rides= new ArrayList<>();

    //Un User peut avoir plusieurs Reservation en tant que passager.
    //table reservation
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations= new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewsGiven= new ArrayList<>(); // Liste des évaluations laissées par l'utilisateur

    @OneToMany(mappedBy = "reviewee")
    private List<Review> reviewsReceived= new ArrayList<>(); // Liste des évaluations reçues par l'utilisateur



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Review> getReviewsGiven() {
        return reviewsGiven;
    }

    public void setReviewsGiven(List<Review> reviewsGiven) {
        this.reviewsGiven = reviewsGiven;
    }

    public List<Review> getReviewsReceived() {
        return reviewsReceived;
    }

    public void setReviewsReceived(List<Review> reviewsReceived) {
        this.reviewsReceived = reviewsReceived;
    }

    public double getAverageRating() {
        if (reviewsReceived== null || reviewsReceived.isEmpty()) {
            return 0.0;
        }
        double totalRating = 0;
        for (Review review : reviewsReceived) {
            totalRating += review.getRating();
        }
        return totalRating / reviewsReceived.size();
    }




}

