package com.K955.Placement_Portal.Service;

import com.K955.Placement_Portal.DTOs.Resume.AtsReportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface AtsService {
    AtsReportResponse analyzeResume(Long userId) throws IOException;

    Page<AtsReportResponse> getAtsHistory(Long userId, String search, Pageable pageable);

    AtsReportResponse getAtsReport(Long reportId, Long userId);
}
