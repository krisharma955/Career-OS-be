package com.K955.Placement_Portal.DTOs.Company;

import jakarta.validation.constraints.NotBlank;

public record fillCompanyRequest(
        @NotBlank String companyName,
        @NotBlank String website,
        @NotBlank String industry,
        @NotBlank String description,
        @NotBlank String location
) {
}
