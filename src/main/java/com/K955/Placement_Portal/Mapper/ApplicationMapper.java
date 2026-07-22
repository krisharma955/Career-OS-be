package com.K955.Placement_Portal.Mapper;

import com.K955.Placement_Portal.DTOs.Application.ApplicationResponse;
import com.K955.Placement_Portal.Entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(source = "student.user.name", target = "studentName")
    @Mapping(source = "student.user.email", target = "studentEmail")
    @Mapping(source = "jobPosting.title", target = "jobTitle")
    @Mapping(source = "jobPosting.company.companyName", target = "companyName")
    @Mapping(source = "jobPosting.jobType", target = "jobType")
    @Mapping(source = "student.user.id", target = "studentId")
    ApplicationResponse toApplicationResponse(Application application);

    List<ApplicationResponse> toListOfApplicationResponse(List<Application> applicationList);

}
