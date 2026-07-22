package com.K955.Placement_Portal.DTOs.Application;

import com.K955.Placement_Portal.Enums.ApplicationStatus;
import com.K955.Placement_Portal.Enums.JobType;

import java.time.Instant;

public record ApplicationResponse(
        Long id,
        ApplicationStatus applicationStatus,
        Instant appliedAt,
        String studentName,
        String studentEmail,
        String jobTitle,
        String companyName,
        JobType jobType,
        Long studentId
) {
}
