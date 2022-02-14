package com.sp.api.question.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.question.dto.QuestionReqDto;
import com.sp.api.question.dto.QuestionResDto;
import com.sp.api.question.service.QuestionService;
import io.swagger.annotations.Api;
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
@Api(value = "QuestionController - 질문 관련 API")
public class QuestionController {

    private final QuestionService questionService;

    @ApiOperation(value = "질문 목록 조회 (미완료)", notes = "조건에 따른 질문 목록 조회")
    @GetMapping("/questions")
    public ApiResponse<List<QuestionResDto>> getQuestions(QuestionReqDto.Search request) {
        return ApiResponse.success(questionService.getQuestions(3L, request));
    }

    @ApiOperation(value = "질문 상세 조회 (미완료)", notes = "질문의 상세 내용 조회")
    @GetMapping("/question/{questionId}")
    public ApiResponse<QuestionResDto> getQuestion(@PathVariable("questionId")Long questionId) {
        return ApiResponse.success(questionService.getQuestion(3L, questionId));
    }

    @ApiOperation(value = "질문 등록 (미완료)", notes = "새로운 질문 등록")
    @PostMapping("/question")
    public ApiResponse<QuestionResDto> createQuestion(@RequestBody @Valid QuestionReqDto.Create request) {
        return ApiResponse.success(questionService.createQuestion(3L, request));
    }

    @ApiOperation(value = "질문 수정 (미완료)", notes = "기존의 질문 수정")
    @PatchMapping("/question/{questionId}")
    public ApiResponse<String> updateQuestion(
        @PathVariable("questionId")Long questionId,
        @RequestBody @Valid QuestionReqDto.Update request
    ) {
        questionService.updateQuestion(3L, questionId, request);

        return ApiResponse.success("OK");
    }

    @ApiOperation(value = "질문 삭제 (미완료)", notes = "기존의 질문 삭제")
    @DeleteMapping("/question/{questionId}")
    public ApiResponse<String> deleteQuestion(@PathVariable("questionId")Long questionId) {
        questionService.deleteQuestion(3L, questionId);

        return ApiResponse.success("OK");
    }
}
