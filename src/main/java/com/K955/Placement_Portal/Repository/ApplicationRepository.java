package com.K955.Placement_Portal.Repository;

import com.K955.Placement_Portal.Entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByStudentId(Long studentId, Pageable pageable);

    Page<Application> findByJobPostingId(Long jobId, Pageable pageable);

    Boolean existsByStudentIdAndJobPostingId(Long studentId, Long jobId);

}
