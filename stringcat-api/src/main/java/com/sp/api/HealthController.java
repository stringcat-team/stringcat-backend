package com.sp.api;

import com.sp.api.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping({"/", "/ping"})
    public ApiResponse<String> health() {
        return ApiResponse.success("OK");
    }
}
