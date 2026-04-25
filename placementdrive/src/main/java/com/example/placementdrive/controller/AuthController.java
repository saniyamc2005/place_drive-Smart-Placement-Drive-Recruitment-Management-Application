package com.example.placementdrive.controller;

import com.example.placementdrive.model.*;
import com.example.placementdrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    // ── Register student ─────────────────────────────────────────────
    // POST /api/auth/register/student
    // Body: { "user": {name, email, password}, "student": {usn, branch, cgpa, phoneNumber, skills} }
    @PostMapping("/register/student")
    public Map<String, Object> registerStudent(@RequestBody Map<String, Object> body) {
        User user = new User();
        Map<String, Object> u = castMap(body.get("user"));
        user.setName((String) u.get("name"));
        user.setEmail((String) u.get("email"));
        user.setPassword((String) u.get("password"));

        Student student = new Student();
        Map<String, Object> s = castMap(body.get("student"));
        student.setUsn((String) s.get("usn"));
        student.setBranch((String) s.get("branch"));
        student.setCgpa(toDouble(s.get("cgpa")));
        if (s.get("phoneNumber") != null) student.setPhoneNumber((String) s.get("phoneNumber"));
        if (s.get("skills") != null)      student.setSkills((String) s.get("skills"));

        return userService.registerStudent(user, student);
    }

    // ── Register company ─────────────────────────────────────────────
    // POST /api/auth/register/company
    // Body: { "user": {name, email, password}, "company": {companyName, description, industry, website, contactEmail} }
    @PostMapping("/register/company")
    public Map<String, Object> registerCompany(@RequestBody Map<String, Object> body) {
        User user = new User();
        Map<String, Object> u = castMap(body.get("user"));
        user.setName((String) u.get("name"));
        user.setEmail((String) u.get("email"));
        user.setPassword((String) u.get("password"));

        Company company = new Company();
        Map<String, Object> c = castMap(body.get("company"));
        company.setCompanyName((String) c.get("companyName"));
        company.setDescription((String) c.get("description"));
        if (c.get("industry") != null)     company.setIndustry((String) c.get("industry"));
        if (c.get("website") != null)      company.setWebsite((String) c.get("website"));
        if (c.get("contactEmail") != null) company.setContactEmail((String) c.get("contactEmail"));

        return userService.registerCompany(user, company);
    }

    // ── Register admin ───────────────────────────────────────────────
    // POST /api/auth/register/admin
    // Body: { name, email, password }
    @PostMapping("/register/admin")
    public Map<String, Object> registerAdmin(@RequestBody User user) {
        return userService.registerAdmin(user);
    }

    // ── Login ────────────────────────────────────────────────────────
    // POST /api/auth/login
    // Body: { email, password }
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        return userService.login(body.get("email"), body.get("password"));
    }

    // ── Helpers ──────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private Map<String, Object> castMap(Object obj) {
        return (Map<String, Object>) obj;
    }

    private double toDouble(Object obj) {
        if (obj instanceof Number) return ((Number) obj).doubleValue();
        return Double.parseDouble(obj.toString());
    }
}