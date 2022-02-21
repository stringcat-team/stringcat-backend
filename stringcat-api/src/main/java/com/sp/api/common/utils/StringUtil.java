package com.sp.api.common.utils;

import org.springframework.http.ContentDisposition;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;


public class StringUtil extends StringUtils {

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String TIME_SEPARATOR = "_";
    private static final String CATEGORY_PREFIX = "/";
    private static final int UNDER_BAR_INDEX = 1;

    public static String buildFileName(String image, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return image + CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + fileExtension;
    }

    public static ContentDisposition createContentDisposition(String imageFileName) {
        String fileName = imageFileName.substring(imageFileName.lastIndexOf(CATEGORY_PREFIX) + UNDER_BAR_INDEX);

        return ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
    }
}