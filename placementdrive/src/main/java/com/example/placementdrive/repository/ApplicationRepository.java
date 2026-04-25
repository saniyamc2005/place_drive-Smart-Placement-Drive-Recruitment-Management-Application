package com.example.placementdrive.repository;

import com.example.placementdrive.model.Application;
import com.example.placementdrive.model.Job;
import com.example.placementdrive.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByStudent(Student student);
    List<Application> findByJob(Job job);
    List<Application> findByStatus(String status);
    boolean existsByStudentAndJob(Student student, Job job);
    long countByStatus(String status);
    long countByJob(Job job);
}