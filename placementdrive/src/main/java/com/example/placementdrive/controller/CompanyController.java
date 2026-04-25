package com.example.placementdrive.controller;

import com.example.placementdrive.model.Company;
import com.example.placementdrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    @Autowired
    private UserService userService;

    // ── Get all companies (admin / students) ─────────────────────────
    // GET /api/companies/all
    @GetMapping("/all")
    public List<Company> getAllCompanies() {
        return userService.getAllCompanies();
    }

    // ── Get company by ID ────────────────────────────────────────────
    // GET /api/companies/{companyId}
    @GetMapping("/{companyId}")
    public Map<String, Object> getById(@PathVariable int companyId) {
        return userService.getCompanyById(companyId);
    }
}