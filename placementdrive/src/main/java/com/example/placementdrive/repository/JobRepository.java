package com.example.placementdrive.repository;

import com.example.placementdrive.model.Company;
import com.example.placementdrive.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByCompany(Company company);
    List<Job> findByStatus(String status);
    List<Job> findByJobType(String jobType);
    List<Job> findByMinCgpaLessThanEqual(double cgpa);
}