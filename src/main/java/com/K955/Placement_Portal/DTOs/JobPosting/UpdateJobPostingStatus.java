package com.K955.Placement_Portal.DTOs.JobPosting;

import com.K955.Placement_Portal.Enums.JobPostingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateJobPostingStatus(
        @NotNull JobPostingStatus jobPostingStatus
) {
}
