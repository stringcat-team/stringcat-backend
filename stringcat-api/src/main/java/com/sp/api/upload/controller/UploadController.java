package com.sp.api.upload.controller;

import com.sp.api.common.dto.ApiResponse;
import com.sp.api.common.utils.StringUtil;
import com.sp.api.upload.service.S3UploadService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class UploadController {

    private final S3UploadService s3UploadService;

    @ApiOperation(value = "파일 업로드를 위한 API")
    @PostMapping(value = "/images")
    public ApiResponse<String> upload(@RequestParam("image") String image, @RequestPart MultipartFile multipartFile) throws IOException {

        String fileRequest = multipartFile.toString();

        log.info("파일 업로드 REQ :: {}", fileRequest);

        String response = s3UploadService.upload(multipartFile, image);

//        String fileResponse = s3UploadService.extractFileExtension(multipartFile.getName());

        log.info("파일 업로드 RES");

        return ApiResponse.success(response);
    }

    @ApiOperation(value = "S3 다운로드 API")
    @PostMapping(value = "/download")
    public ApiResponse<ByteArrayResource> downloadFile(@RequestParam("resourcePath") String resourcePath) {
        byte[] data = s3UploadService.downloadFile(resourcePath);
        ByteArrayResource resource = new ByteArrayResource(data);
        HttpHeaders headers = buildHeaders(resourcePath, data);

        return ApiResponse.success(resource);
    }

    private HttpHeaders buildHeaders(String resourcePath, byte[] data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(data.length);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(StringUtil.createContentDisposition(resourcePath));

        return headers;
    }

}