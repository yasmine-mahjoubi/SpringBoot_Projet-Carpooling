package com.fst.projet_CarPooling_jee.Service.impl;

import com.fst.projet_CarPooling_jee.Entity.Review;
import com.fst.projet_CarPooling_jee.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Créer une review
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    // Récupérer une review par ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Récupérer toutes les reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Supprimer une review par ID
    public void deleteReviewById(Long id) {
        reviewRepository.deleteById(id);
    }
}

