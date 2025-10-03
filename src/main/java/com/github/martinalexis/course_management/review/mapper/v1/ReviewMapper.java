package com.github.martinalexis.course_management.review.mapper.v1;

import com.github.martinalexis.course_management.review.dto.v1.CreateReviewRequestDto;
import com.github.martinalexis.course_management.review.dto.v1.CreateReviewResponseDto;
import com.github.martinalexis.course_management.review.model.ReviewModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewModel createReviewRequestToEntity(CreateReviewRequestDto dto);

    CreateReviewResponseDto createReviewRequestToDto(ReviewModel entity);
}
