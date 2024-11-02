package com.example.myvoca.dto;

import com.example.myvoca.entity.Vocab;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EditVocab {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotNull
        @Size(max=16)
        private String title;
        @Size(max=32)
        private String description;
    }
}