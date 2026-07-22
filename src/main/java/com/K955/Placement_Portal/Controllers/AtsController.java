package com.K955.Placement_Portal.Controllers;

import com.K955.Placement_Portal.DTOs.Resume.AtsReportResponse;
import com.K955.Placement_Portal.Security.JwtUtil;
import com.K955.Placement_Portal.Service.AtsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resume/analyze")
public class AtsController {

    private final AtsService atsService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<AtsReportResponse> analyzeResume() throws IOException {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(atsService.analyzeResume(userId));
    }

    @GetMapping("/history")
    public ResponseEntity<Page<AtsReportResponse>> getAtsHistory(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(atsService.getAtsHistory(userId, search, pageable));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<AtsReportResponse> getAtsReport(@PathVariable Long reportId) {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(atsService.getAtsReport(reportId, userId));
    }

}
