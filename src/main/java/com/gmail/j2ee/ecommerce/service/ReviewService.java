package com.gmail.j2ee.ecommerce.service;

import com.gmail.j2ee.ecommerce.domain.Review;

import java.util.List;

public interface ReviewService {

    List<Review> getReviewsByPerfumeId(Long perfumeId);

    Review addReviewToPerfume(Review review, Long perfumeId);
}
