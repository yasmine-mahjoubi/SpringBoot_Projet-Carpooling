package com.fst.projet_CarPooling_jee.Controller;

import com.fst.projet_CarPooling_jee.Entity.Reservation;
import com.fst.projet_CarPooling_jee.Entity.Ride;
import com.fst.projet_CarPooling_jee.Entity.User;
import com.fst.projet_CarPooling_jee.Entity.enums.ReservationStatus;
import com.fst.projet_CarPooling_jee.Repository.UserRepository;
import com.fst.projet_CarPooling_jee.Service.impl.ReservationService;
import com.fst.projet_CarPooling_jee.Service.impl.RideService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private RideService rideService;

    @Autowired
    private UserRepository userRepository;

    // Affiche la liste des réservations
    @GetMapping("/reservations")
    public String viewReservations(Model model) {
        model.addAttribute("listReservations", reservationService.getAllReservations());
        return "reservations"; // Vue HTML pour afficher les réservations
    }

    // Sauvegarde une nouvelle réservation
    @PostMapping("/saveReservation")
    public String saveReservation(@RequestParam("rideId") Long rideId,
                                  @RequestParam("reservedPlaces") int reservedPlaces,
                                  HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            reservationService.saveReservation(rideId, userId, reservedPlaces);
        } catch (RuntimeException e) {
            return "redirect:/searchRides?error=" + e.getMessage();
        }

        return "redirect:/searchRides";
    }

    // Mise à jour du statut d'une réservation
    @PostMapping("/updateReservationStatus")
    public String updateReservationStatus(
            @RequestParam("id") Long id,
            @RequestParam("status") ReservationStatus status
    ) {
        reservationService.updateReservationStatus(id, status);
        return "redirect:/reservations"; // Redirection après la mise à jour
    }

    @PostMapping("/deleteReservation")
    public String deleteReservation(@RequestParam("id") Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }

}
