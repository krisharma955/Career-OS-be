package com.K955.Placement_Portal.DTOs.Student;

import jakarta.validation.constraints.*;

import java.util.List;

public record fillStudentRequest(
        @NotBlank String phoneNumber,

        @NotBlank String college,

        @NotBlank String degree,

        @NotBlank String branch,

        @Min(2000)
        @Max(2040)
        @NotNull Integer graduationYear,

        @DecimalMax("10.0")
        @DecimalMin("0.0")
        @NotNull Double cgpa,

        List<String> skills
) {
}
