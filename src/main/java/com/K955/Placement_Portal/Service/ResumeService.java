package com.K955.Placement_Portal.Service;

import com.K955.Placement_Portal.DTOs.Resume.ResumeResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ResumeService {
    ResumeResponse uploadResume(Long userId, MultipartFile file) throws IOException;

    ResumeResponse getResumeMetaData(Long userId);

    String downloadResume(Long userId) throws MalformedURLException;
}
