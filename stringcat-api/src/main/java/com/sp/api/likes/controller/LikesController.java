package com.sp.api.likes.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.likes.dto.LikesPushRes;
import com.sp.api.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/question/{questionId}/like")
    public ApiResponse<LikesPushRes> questionLikePush(@PathVariable("questionId") Long questionId) {
        return ApiResponse.success(likesService.questionLikePush(3L, questionId));
    }

    @PostMapping("/question/{questionId}/dis-like")
    public ApiResponse<LikesPushRes> questionDisLikePush(
        @PathVariable("questionId") Long questionId) {
        return ApiResponse.success(likesService.questionDisLikePush(3L, questionId));
    }

    @PostMapping("/answer/{answerId}/like")
    public ApiResponse<LikesPushRes> answerLikePush(@PathVariable("answerId") Long answerId) {
        return ApiResponse.success(likesService.answerLikePush(3L, answerId));
    }

    @PostMapping("/answer/{answerId}/dis-like")
    public ApiResponse<LikesPushRes> answerDisLikePush(@PathVariable("answerId") Long answerId) {
        return ApiResponse.success(likesService.answerDisLikePush(3L, answerId));
    }
}