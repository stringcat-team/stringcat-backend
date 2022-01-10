package com.sp.api.common.utils;

import com.sp.common.type.HttpStatusCode;

public class ApiResponse<T> {

    private String status;

    private String message;

    private String redirect = "";

    private T t;

    public ApiResponse(HttpStatusCode status) {
        this.status = status.getDesc();
    }

    public ApiResponse(HttpStatusCode status, String message) {
        this.status = status.getDesc();
        this.message = message;
    }

    public ApiResponse(HttpStatusCode status, String message, T t) {
        this.status = status.getDesc();
        this.message = message;
        this.t = t;
    }

    public static ApiResponse ok() {
        return new ApiResponse(HttpStatusCode.OK, "ok");
    }

    public static ApiResponse ok(Object object) {
        return new ApiResponse(HttpStatusCode.OK, "ok", object);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(HttpStatusCode.INTERNAL_SERVER, message);
    }

    public static ApiResponse none_auth() {
        return new ApiResponse(HttpStatusCode.UNAUTHORIZED, "noneAuth");
    }


}