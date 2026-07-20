package com.K955.Placement_Portal.Controllers;

import com.K955.Placement_Portal.DTOs.Company.CompanyProfileRequest;
import com.K955.Placement_Portal.DTOs.Company.CompanyProfileResponse;
import com.K955.Placement_Portal.DTOs.Company.UpdateCompanyRequest;
import com.K955.Placement_Portal.DTOs.Company.fillCompanyRequest;
import com.K955.Placement_Portal.Security.JwtUtil;
import com.K955.Placement_Portal.Service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final JwtUtil jwtUtil;

    @PostMapping("/profile")
    public ResponseEntity<CompanyProfileResponse> createCompanyProfile(@Valid @RequestBody CompanyProfileRequest request) {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompanyProfile(userId, request));
    }

    @GetMapping("/profile")
    public ResponseEntity<CompanyProfileResponse> getCompanyProfile() {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(companyService.getCompanyProfileById(userId));
    }

    @PatchMapping
    public ResponseEntity<CompanyProfileResponse> updateCompanyProfile(@Valid @RequestBody UpdateCompanyRequest request) {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(companyService.updateCompanyProfile(userId, request));
    }

    @PatchMapping("/profile")
    public ResponseEntity<CompanyProfileResponse> fillCompanyProfile(@Valid @RequestBody fillCompanyRequest request) {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(companyService.fillCompanyProfile(userId, request));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteCompanyProfile() {
        Long userId = jwtUtil.getCurrentUserId();
        companyService.deleteCompanyProfileById(userId);
        return ResponseEntity.noContent().build();
    }

}
