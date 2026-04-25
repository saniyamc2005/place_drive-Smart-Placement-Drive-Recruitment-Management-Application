package com.example.placementdrive.repository;

import com.example.placementdrive.model.Company;
import com.example.placementdrive.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByUser(User user);
    Optional<Company> findByCompanyName(String companyName);
}