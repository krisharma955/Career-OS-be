package com.K955.Placement_Portal.Controllers;

import com.K955.Placement_Portal.DTOs.Resume.ResumeResponse;
import com.K955.Placement_Portal.Security.JwtUtil;
import com.K955.Placement_Portal.Service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final JwtUtil jwtUtil;

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponse> uploadResume(@RequestParam("file") MultipartFile file) throws IOException {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(resumeService.uploadResume(userId, file));
    }

    @GetMapping
    public ResponseEntity<ResumeResponse> getResume() {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(resumeService.getResumeMetaData(userId));
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadResume() throws IOException {
        Long userId = jwtUtil.getCurrentUserId();

        ResumeResponse metaData = resumeService.getResumeMetaData(userId);
        Resource resource = resumeService.downloadResume(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + metaData.fileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

}
