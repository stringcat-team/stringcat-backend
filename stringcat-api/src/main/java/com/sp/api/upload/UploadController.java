package com.sp.api.upload;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.common.utils.S3Uploader;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class UploadController {

    private final S3Uploader s3Uploader;

    @ApiOperation(value = "파일 업로드를 위한 API")
    @PostMapping(value = "/images")
    public ApiResponse<String> upload(@RequestPart MultipartFile multipartFile) throws IOException {

        String fileRequest = multipartFile.toString();

        log.info("파일 업로드 REQ :: {}", fileRequest);

        s3Uploader.upload(multipartFile, "images/origin");

        String fileResponse = s3Uploader.extractFileExtension(multipartFile.getName());

        log.info("파일 업로드 RES :: {}", fileResponse);

        return ApiResponse.success(fileResponse);
    }

}
