package com.K955.Placement_Portal.Service;

import com.K955.Placement_Portal.DTOs.Application.ApplicationResponse;
import com.K955.Placement_Portal.DTOs.Application.UpdateApplicationStatusRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse createApplication(Long userId, Long jobId);

    ApplicationResponse getApplicationById(Long applicationId);

    Page<ApplicationResponse> getMyApplications(Long userId, Pageable pageable);

    Page<ApplicationResponse> getApplicationsByJob(Long jobId, Pageable pageable);

    ApplicationResponse updateApplicationStatus(Long applicationId, @Valid UpdateApplicationStatusRequest request);

    void withdrawApplication(Long applicationId);
}
