package com.fst.projet_CarPooling_jee.Repository;

import com.fst.projet_CarPooling_jee.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRideId(Long rideId);
}
