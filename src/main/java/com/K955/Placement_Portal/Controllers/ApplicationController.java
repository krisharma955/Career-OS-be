package com.K955.Placement_Portal.Controllers;

import com.K955.Placement_Portal.DTOs.Application.ApplicationResponse;
import com.K955.Placement_Portal.DTOs.Application.UpdateApplicationStatusRequest;
import com.K955.Placement_Portal.Security.JwtUtil;
import com.K955.Placement_Portal.Service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/jobs/{jobId}/applications")
    public ResponseEntity<ApplicationResponse> createApplication(@PathVariable Long jobId) {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.createApplication(userId, jobId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(applicationService.getApplicationById(applicationId));
    }

    @GetMapping("/me/applications")
    public ResponseEntity<Page<ApplicationResponse>> getMyApplications(
            @PageableDefault(size = 5, sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(applicationService.getMyApplications(userId, pageable));
    }

    @GetMapping("/jobs/{jobId}/applications") //applications for this job -- can be accessed by company which created job
    public ResponseEntity<Page<ApplicationResponse>> getApplicationsByJob(
            @PathVariable Long jobId,
            @PageableDefault(size = 5, sort = "appliedAt", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId, pageable));
    }

    @PatchMapping("/applications/{applicationId}")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(@PathVariable Long applicationId,
                                                                       @Valid @RequestBody UpdateApplicationStatusRequest request) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, request));
    }

    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<Void> withdrawApplication(@PathVariable Long applicationId) {
        applicationService.withdrawApplication(applicationId);
        return ResponseEntity.noContent().build();
    }

}
