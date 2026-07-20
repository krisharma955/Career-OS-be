package com.K955.Placement_Portal.Service.ImpL;

import com.K955.Placement_Portal.DTOs.Company.CompanyProfileRequest;
import com.K955.Placement_Portal.DTOs.Company.CompanyProfileResponse;
import com.K955.Placement_Portal.DTOs.Company.UpdateCompanyRequest;
import com.K955.Placement_Portal.DTOs.Company.fillCompanyRequest;
import com.K955.Placement_Portal.Entity.Company;
import com.K955.Placement_Portal.Entity.User;
import com.K955.Placement_Portal.Exceptions.BadRequestException;
import com.K955.Placement_Portal.Exceptions.ResourceNotFoundException;
import com.K955.Placement_Portal.Mapper.CompanyMapper;
import com.K955.Placement_Portal.Repository.CompanyRepository;
import com.K955.Placement_Portal.Repository.UserRepository;
import com.K955.Placement_Portal.Service.CompanyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyServiceImpL implements CompanyService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyProfileResponse createCompanyProfile(Long userId, CompanyProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        Boolean check = companyRepository.existsByUserId(userId);
        if(check) throw new BadRequestException("Company with userId: " +userId+ " already exists.");

        Company company = companyMapper.toCompany(request);
        company.setUser(user);
        companyRepository.save(company);

        return companyMapper.toCompanyProfileResponse(company);
    }

    @Override
    public CompanyProfileResponse getCompanyProfileById(Long userId) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", userId.toString()));

        return companyMapper.toCompanyProfileResponse(company);
    }

    @Override
    public CompanyProfileResponse updateCompanyProfile(Long userId, UpdateCompanyRequest request) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", userId.toString()));

        if(request.companyName() != null) company.setCompanyName(request.companyName());
        if(request.website() != null) company.setWebsite(request.website());
        if(request.industry() != null) company.setIndustry(request.industry());
        if(request.description() != null) company.setDescription(request.description());
        if(request.location() != null) company.setLocation(request.location());

        Company saved = companyRepository.save(company);

        return companyMapper.toCompanyProfileResponse(saved);
    }

    @Override
    public CompanyProfileResponse fillCompanyProfile(Long userId, fillCompanyRequest request) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", userId.toString()));

        company.setCompanyName(request.companyName());
        company.setWebsite(request.website());
        company.setIndustry(request.industry());
        company.setDescription(request.description());
        company.setLocation(request.location());

        Company saved = companyRepository.save(company);

        return companyMapper.toCompanyProfileResponse(saved);
    }

    @Override
    public void deleteCompanyProfileById(Long userId) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", userId.toString()));

        companyRepository.delete(company);
    }

}
