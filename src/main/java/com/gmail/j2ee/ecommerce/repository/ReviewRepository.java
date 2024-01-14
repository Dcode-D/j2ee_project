package com.gmail.j2ee.ecommerce.repository;

import com.gmail.j2ee.ecommerce.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
