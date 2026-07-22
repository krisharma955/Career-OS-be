package com.K955.Placement_Portal.Service.ImpL;

import com.K955.Placement_Portal.DTOs.Application.ApplicationResponse;
import com.K955.Placement_Portal.DTOs.Application.UpdateApplicationStatusRequest;
import com.K955.Placement_Portal.Entity.Application;
import com.K955.Placement_Portal.Entity.JobPosting;
import com.K955.Placement_Portal.Entity.Student;
import com.K955.Placement_Portal.Entity.User;
import com.K955.Placement_Portal.Enums.JobPostingStatus;
import com.K955.Placement_Portal.Enums.Role;
import com.K955.Placement_Portal.Exceptions.BadRequestException;
import com.K955.Placement_Portal.Exceptions.ResourceNotFoundException;
import com.K955.Placement_Portal.Mapper.ApplicationMapper;
import com.K955.Placement_Portal.Repository.ApplicationRepository;
import com.K955.Placement_Portal.Repository.JobPostingRepository;
import com.K955.Placement_Portal.Repository.StudentRepository;
import com.K955.Placement_Portal.Repository.UserRepository;
import com.K955.Placement_Portal.Security.JwtUtil;
import com.K955.Placement_Portal.Service.ApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationServiceImpL implements ApplicationService {

    private final StudentRepository studentRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public ApplicationResponse createApplication(Long userId, Long jobId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", userId.toString()));

        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", jobId.toString()));

        Boolean check = applicationRepository.existsByStudentIdAndJobPostingId(student.getId(), jobId);
        if(check) throw new BadRequestException("Student has already applied to this job");

        if(jobPosting.getJobPostingStatus().equals(JobPostingStatus.CLOSE)) {
            throw new BadRequestException("Job posting is closed");
        }

        Application application = Application.builder()
                .student(student)
                .jobPosting(jobPosting)
                .build();

        applicationRepository.save(application);

        return applicationMapper.toApplicationResponse(application);
    }

    @Override
    public ApplicationResponse getApplicationById(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", applicationId.toString()));
        return applicationMapper.toApplicationResponse(application);
    }

    @Override
    public Page<ApplicationResponse> getMyApplications(Long userId, Pageable pageable) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", userId.toString()));
        return applicationRepository.findByStudentId(student.getId(), pageable)
                .map(applicationMapper::toApplicationResponse);
    }

    @Override
    public Page<ApplicationResponse> getApplicationsByJob(Long jobId, Pageable pageable) {
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", jobId.toString()));

        Long currentUserId = jwtUtil.getCurrentUserId();
        if (!jobPosting.getCompany().getUser().getId().equals(currentUserId)) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to view applications for this job.");
        }

        return applicationRepository.findByJobPostingId(jobPosting.getId(), pageable)
                .map(applicationMapper::toApplicationResponse);
    }

    @Override
    public ApplicationResponse updateApplicationStatus(Long applicationId, UpdateApplicationStatusRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", applicationId.toString()));

        updateApplicationStatusCheck(application);

        application.setApplicationStatus(request.applicationStatus());
        Application saved = applicationRepository.save(application);

        return applicationMapper.toApplicationResponse(saved);
    }

    @Override
    public void withdrawApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", applicationId.toString()));
        withdrawApplicationCheck(application);
        applicationRepository.delete(application);
    }

    /// Util Methods

    private void updateApplicationStatusCheck(Application application) {
        Long userId = jwtUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        if(!user.getRole().equals(Role.COMPANY)) {
            throw new BadRequestException("Students are not allowed to update Application.");
        }

        if(!application.getJobPosting().getCompany().getUser().getId().equals(userId)) {
            throw new BadRequestException("Inaccessible Request!");
        }
    }

    private void withdrawApplicationCheck(Application application) {
        Long userId = jwtUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        if(!user.getRole().equals(Role.STUDENT)) {
            throw new BadRequestException("Only Students can withdraw their Application.");
        }

        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", userId.toString()));

        if(!application.getStudent().getUser().getId().equals(userId)) {
            throw new BadRequestException("Inaccessible Request!");
        }
    }

}
