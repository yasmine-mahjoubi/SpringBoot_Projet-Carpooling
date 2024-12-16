package com.fst.projet_CarPooling_jee.Controller;

import com.fst.projet_CarPooling_jee.Entity.Review;
import com.fst.projet_CarPooling_jee.Entity.Ride;
import com.fst.projet_CarPooling_jee.Entity.User;

import com.fst.projet_CarPooling_jee.Service.impl.ReviewService;
import com.fst.projet_CarPooling_jee.Service.impl.RideService;
import com.fst.projet_CarPooling_jee.Service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RideService rideService;

    @Autowired
    private UserService userService;

    // Ajouter une review
    @PostMapping("/addReview")
    public String addReview(@RequestParam("rideId") Long rideId,
                            @RequestParam("userId") Long userId,
                            @RequestParam("comment") String comment,
                            @RequestParam("rating") int rating) {
        // Créer une nouvelle instance de Review
        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);

        // Récupérer le trajet et l'utilisateur depuis les services
        Ride ride = rideService.getRideById(rideId);
        User reviewer = userService.findById(userId);

        // Associer les objets récupérés
        review.setRide(ride);
        review.setReviewer(reviewer);
        review.setReviewee(ride.getDriver()); // Le driver est l'utilisateur évalué

        // Sauvegarder la review dans la base de données
        reviewService.createReview(review);

        // Rediriger vers la liste des réservations
        return "redirect:/reservations";
    }

    // Supprimer une review par ID
    @PostMapping("/deleteReview")
    public String deleteReview(@RequestParam("id") Long id) {
        reviewService.deleteReviewById(id);
        return "redirect:/reviews";
    }

    // Afficher toutes les reviews (optionnel)
    @GetMapping("/reviews")
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "reviews"; // Page Thymeleaf pour afficher les reviews
    }
}
