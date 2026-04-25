package com.example.placementdrive.repository;

import com.example.placementdrive.model.Student;
import com.example.placementdrive.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUser(User user);
    Optional<Student> findByUsn(String usn);
    List<Student> findByBranch(String branch);
    List<Student> findByCgpaGreaterThanEqual(double cgpa);
}