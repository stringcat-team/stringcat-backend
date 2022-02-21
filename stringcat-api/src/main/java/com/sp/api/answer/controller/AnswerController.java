package com.sp.api.answer.controller;

import com.sp.api.answer.dto.AnswerReqDto;
import com.sp.api.answer.dto.AnswerResDto;
import com.sp.api.answer.service.AnswerService;
import com.sp.api.common.dto.ApiResponse;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @ApiOperation(value = "답변 목록 조회 (완료)", notes = "답변 목록 조회")
    @GetMapping("/question/{questionId}/answers")
    public ApiResponse<List<AnswerResDto>> getAnswers(@PathVariable("questionId")Long questionId) {
        return ApiResponse.success(answerService.getAnswers(questionId, 3L));
    }

    @ApiOperation(value = "답변 등록 (완료)", notes = "답변 등록")
    @PostMapping("/question/{questionId}/answer")
    public ApiResponse<AnswerResDto> createAnswer(
        @PathVariable("questionId")Long questionId,
        @RequestBody @Valid AnswerReqDto.Create request
    ) {
        return ApiResponse.success(answerService.createAnswer(questionId, 3L, request));
    }

    @ApiOperation(value = "답변 수정 (완료)", notes = "기존의 답변 수정")
    @PatchMapping("/answer/{answerId}")
    public ApiResponse<String> updateAnswer(
        @PathVariable("answerId")Long answerId,
        @RequestBody @Valid AnswerReqDto.Update request
    ) {
        answerService.updateAnswer(answerId, 3L, request);
        return ApiResponse.success("OK");
    }

    @ApiOperation(value = "답변 삭제(완료)", notes = "기존의 답변 삭제")
    @DeleteMapping("/answer/{answerId}")
    public ApiResponse<String> deleteAnswer(@PathVariable("answerId")Long answerId) {
        answerService.deleteAnswer(answerId, 3L);
        return ApiResponse.success("OK");
    }
}
