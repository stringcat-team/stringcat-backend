package com.sp.api.upload.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.sp.api.common.utils.StringUtil;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3UploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public void uploadFile(String image, MultipartFile multipartFile) {
        validateFileExists(multipartFile);

        String fileName = StringUtil.buildFileName(image, Objects.requireNonNull(multipartFile.getOriginalFilename()));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
        } catch (IOException e) {
            throw new StringcatCustomException("파일 업로드가 실패하였습니다.", ErrorCode.FAILED_TO_UPLOAD_FILE);
        }

        amazonS3Client.getUrl(bucket, fileName);
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            throw new StringcatCustomException("파일이 등록되지 않았습니다.", ErrorCode.INVALID_FILE_NAME);
        }
    }

    public byte[] downloadFile(String resourcePath) {
        validateFileExistAtUrl(resourcePath);

        S3Object s3Object = amazonS3Client.getObject(bucket, resourcePath);
        S3ObjectInputStream s3InputStream = s3Object.getObjectContent();

        try {
            return IOUtils.toByteArray(s3InputStream);
        } catch (IOException e) {
            throw new StringcatCustomException("파일을 업로드 할 수 없습니다.", ErrorCode.FAILED_TO_UPLOAD_FILE);
        }
    }

    private void validateFileExistAtUrl(String resourcePath) {
        if(!amazonS3Client.doesObjectExist(bucket, resourcePath)) {
            throw new StringcatCustomException("파일을 찾을 수 없습니다.", ErrorCode.NOT_FOUND);
        }
    }

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile));	// PublicRead 권한으로 업로드 됨
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public String extractFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch(StringIndexOutOfBoundsException e) {
            throw new StringcatCustomException("잘못된 형식의 파일이름입니다.", ErrorCode.INVALID_FILE_NAME);
        }
    }
}
