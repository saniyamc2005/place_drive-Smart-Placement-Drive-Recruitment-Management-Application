package com.example.placementdrive.service;

import com.example.placementdrive.model.*;
import com.example.placementdrive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired private UserRepository userRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private CompanyRepository companyRepo;

    // ── Register student ─────────────────────────────────────────────
    public Map<String, Object> registerStudent(User user, Student student) {
        Map<String, Object> res = new HashMap<>();

        if (userRepo.existsByEmail(user.getEmail())) {
            res.put("success", false);
            res.put("message", "Email already registered");
            return res;
        }

        user.setRole("student");
        User savedUser = userRepo.save(user);
        student.setUser(savedUser);
        Student savedStudent = studentRepo.save(student);

        res.put("success", true);
        res.put("message", "Student registered successfully");
        res.put("userId", savedUser.getId());
        res.put("studentId", savedStudent.getId());
        res.put("role", "student");
        return res;
    }

    // ── Register company ─────────────────────────────────────────────
    public Map<String, Object> registerCompany(User user, Company company) {
        Map<String, Object> res = new HashMap<>();

        if (userRepo.existsByEmail(user.getEmail())) {
            res.put("success", false);
            res.put("message", "Email already registered");
            return res;
        }

        user.setRole("company");
        User savedUser = userRepo.save(user);
        company.setUser(savedUser);
        Company savedCompany = companyRepo.save(company);

        res.put("success", true);
        res.put("message", "Company registered successfully");
        res.put("userId", savedUser.getId());
        res.put("companyId", savedCompany.getId());
        res.put("role", "company");
        return res;
    }

    // ── Register admin ───────────────────────────────────────────────
    public Map<String, Object> registerAdmin(User user) {
        Map<String, Object> res = new HashMap<>();

        if (userRepo.existsByEmail(user.getEmail())) {
            res.put("success", false);
            res.put("message", "Email already registered");
            return res;
        }

        user.setRole("admin");
        User savedUser = userRepo.save(user);

        res.put("success", true);
        res.put("message", "Admin registered successfully");
        res.put("userId", savedUser.getId());
        res.put("role", "admin");
        return res;
    }

    // ── Login ────────────────────────────────────────────────────────
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> res = new HashMap<>();

        Optional<User> userOpt = userRepo.findByEmailAndPassword(email, password);
        if (userOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Invalid email or password");
            return res;
        }

        User user = userOpt.get();
        res.put("success", true);
        res.put("message", "Login successful");
        res.put("userId", user.getId());
        res.put("name", user.getName());
        res.put("email", user.getEmail());
        res.put("role", user.getRole());

        // Attach profile details based on role
        if ("student".equals(user.getRole())) {
            studentRepo.findByUser(user).ifPresent(s -> {
                res.put("studentId", s.getId());
                res.put("branch", s.getBranch());
                res.put("cgpa", s.getCgpa());
                res.put("usn", s.getUsn());
            });
        } else if ("company".equals(user.getRole())) {
            companyRepo.findByUser(user).ifPresent(c -> {
                res.put("companyId", c.getId());
                res.put("companyName", c.getCompanyName());
            });
        }
        return res;
    }

    // ── Get all students (admin) ──────────────────────────────────────
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    // ── Get student by ID ────────────────────────────────────────────
    public Map<String, Object> getStudentById(int id) {
        Map<String, Object> res = new HashMap<>();
        Optional<Student> s = studentRepo.findById(id);
        if (s.isEmpty()) {
            res.put("success", false);
            res.put("message", "Student not found");
            return res;
        }
        res.put("success", true);
        res.put("student", s.get());
        return res;
    }

    // ── Update student profile ───────────────────────────────────────
    public Map<String, Object> updateStudentProfile(int studentId, Student updated) {
        Map<String, Object> res = new HashMap<>();
        Optional<Student> opt = studentRepo.findById(studentId);
        if (opt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Student not found");
            return res;
        }
        Student s = opt.get();
        if (updated.getBranch() != null)      s.setBranch(updated.getBranch());
        if (updated.getCgpa() > 0)            s.setCgpa(updated.getCgpa());
        if (updated.getPhoneNumber() != null) s.setPhoneNumber(updated.getPhoneNumber());
        if (updated.getSkills() != null)      s.setSkills(updated.getSkills());
        if (updated.getResumeLink() != null)  s.setResumeLink(updated.getResumeLink());
        studentRepo.save(s);
        res.put("success", true);
        res.put("message", "Profile updated successfully");
        res.put("student", s);
        return res;
    }

    // ── Get all companies (admin) ─────────────────────────────────────
    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    // ── Get company by ID ────────────────────────────────────────────
    public Map<String, Object> getCompanyById(int id) {
        Map<String, Object> res = new HashMap<>();
        Optional<Company> c = companyRepo.findById(id);
        if (c.isEmpty()) {
            res.put("success", false);
            res.put("message", "Company not found");
            return res;
        }
        res.put("success", true);
        res.put("company", c.get());
        return res;
    }
}