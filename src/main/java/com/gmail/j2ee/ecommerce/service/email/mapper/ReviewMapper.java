package com.gmail.j2ee.ecommerce.service.email.mapper;

import com.gmail.j2ee.ecommerce.domain.Review;
import com.gmail.j2ee.ecommerce.dto.review.ReviewRequest;
import com.gmail.j2ee.ecommerce.dto.review.ReviewResponse;
import com.gmail.j2ee.ecommerce.service.ReviewService;
import com.gmail.j2ee.ecommerce.exception.InputFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final CommonMapper commonMapper;
    private final ReviewService reviewService;

    public List<ReviewResponse> getReviewsByPerfumeId(Long perfumeId) {
        return commonMapper.convertToResponseList(reviewService.getReviewsByPerfumeId(perfumeId), ReviewResponse.class);
    }

    public ReviewResponse addReviewToPerfume(ReviewRequest reviewRequest, Long perfumeId, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        Review review = commonMapper.convertToEntity(reviewRequest, Review.class);
        return commonMapper.convertToResponse(reviewService.addReviewToPerfume(review, perfumeId), ReviewResponse.class);
    }
}
