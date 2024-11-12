package com.example.myvoca.dto;

import com.example.myvoca.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        @Size(max=32)
        private String username;
        @NotBlank
        @Size(max=32)
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Integer userId;
        private String username;
        public static LoginDto.Response fromEntity(User user) {
            return Response.builder()
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .build();
        }
    }
}
