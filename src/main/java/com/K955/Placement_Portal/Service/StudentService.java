package com.K955.Placement_Portal.Service;

import com.K955.Placement_Portal.DTOs.Student.StudentProfileRequest;
import com.K955.Placement_Portal.DTOs.Student.StudentProfileResponse;
import com.K955.Placement_Portal.DTOs.Student.UpdateStudentRequest;
import com.K955.Placement_Portal.DTOs.Student.fillStudentRequest;
import jakarta.validation.Valid;

public interface StudentService {
    StudentProfileResponse createStudentProfile(Long userId, @Valid StudentProfileRequest request);

    StudentProfileResponse getStudentProfile(Long userId);

    StudentProfileResponse fillStudentProfile(Long userId, @Valid fillStudentRequest request);

    void deleteStudentProfileById(Long userId);

    StudentProfileResponse updateStudentProfile(Long userId, @Valid UpdateStudentRequest request);
}
