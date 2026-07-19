package com.K955.Placement_Portal.DTOs.Auth;

import com.K955.Placement_Portal.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(

        @NotBlank
        String token

) {
}
