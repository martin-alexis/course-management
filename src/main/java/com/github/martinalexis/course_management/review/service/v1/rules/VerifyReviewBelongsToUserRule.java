package com.github.martinalexis.course_management.review.service.v1.rules;

import com.github.martinalexis.course_management.review.exception.v1.ReviewDoesNotBelongToUserException;
import com.github.martinalexis.course_management.review.model.ReviewModel;
import com.github.martinalexis.course_management.user.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VerifyReviewBelongsToUserRule {

    public void execute(ReviewModel review, UserModel currentUser) {
        if (!review.getUser().getIdUser().equals(currentUser.getIdUser())) {
            throw new ReviewDoesNotBelongToUserException();
        }
    }
}
