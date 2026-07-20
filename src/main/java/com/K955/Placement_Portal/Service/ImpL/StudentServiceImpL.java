package com.K955.Placement_Portal.Service.ImpL;

import com.K955.Placement_Portal.DTOs.Student.StudentProfileRequest;
import com.K955.Placement_Portal.DTOs.Student.StudentProfileResponse;
import com.K955.Placement_Portal.DTOs.Student.UpdateStudentRequest;
import com.K955.Placement_Portal.DTOs.Student.fillStudentRequest;
import com.K955.Placement_Portal.Entity.Student;
import com.K955.Placement_Portal.Entity.User;
import com.K955.Placement_Portal.Exceptions.BadRequestException;
import com.K955.Placement_Portal.Exceptions.ResourceNotFoundException;
import com.K955.Placement_Portal.Mapper.StudentMapper;
import com.K955.Placement_Portal.Repository.StudentRepository;
import com.K955.Placement_Portal.Repository.UserRepository;
import com.K955.Placement_Portal.Service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpL implements StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentProfileResponse createStudentProfile(Long userId, StudentProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        Boolean check = studentRepository.existsByUserId(userId);
        if(check) throw new BadRequestException("Student with userId: " +userId+ " already exists.");

        Student student = studentMapper.toStudent(request);
        student.setUser(user);
        student.setProfileComplete(true);
        studentRepository.save(student);

        return studentMapper.toStudentProfileResponse(student);
    }

    @Override
    public StudentProfileResponse getStudentProfile(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", userId.toString()));

        return studentMapper.toStudentProfileResponse(student);
    }

    @Override
    public StudentProfileResponse fillStudentProfile(Long userId, fillStudentRequest request) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", userId.toString()));

        student.setPhoneNumber(request.phoneNumber());
        student.setCollege(request.college());
        student.setDegree(request.degree());
        student.setBranch(request.branch());
        student.setGraduationYear(request.graduationYear());
        student.setCgpa(request.cgpa());
        if(request.skills() != null) student.setSkills(request.skills());

        Student saved = studentRepository.save(student);

        return studentMapper.toStudentProfileResponse(saved);
    }

    @Override
    public StudentProfileResponse updateStudentProfile(Long userId, UpdateStudentRequest request) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", userId.toString()));

        if(request.phoneNumber() != null) student.setPhoneNumber(request.phoneNumber());
        if(request.college() != null) student.setCollege(request.college());
        if(request.degree() != null) student.setDegree(request.degree());
        if(request.branch() != null) student.setBranch(request.branch());
        if(request.graduationYear() != null) student.setGraduationYear(request.graduationYear());
        if(request.cgpa() != null) student.setCgpa(request.cgpa());
        if(request.skills() != null) student.setSkills(request.skills());

        Student saved = studentRepository.save(student);

        return studentMapper.toStudentProfileResponse(saved);
    }

    @Override
    public void deleteStudentProfileById(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", userId.toString()));

        studentRepository.delete(student);
    }

}
