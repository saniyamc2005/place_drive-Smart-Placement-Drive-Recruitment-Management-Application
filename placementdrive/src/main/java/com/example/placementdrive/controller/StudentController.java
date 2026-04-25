package com.example.placementdrive.controller;

import com.example.placementdrive.model.Student;
import com.example.placementdrive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private UserService userService;

    // ── Get all students (admin) ─────────────────────────────────────
    // GET /api/students/all
    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return userService.getAllStudents();
    }

    // ── Get student by ID ────────────────────────────────────────────
    // GET /api/students/{studentId}
    @GetMapping("/{studentId}")
    public Map<String, Object> getById(@PathVariable int studentId) {
        return userService.getStudentById(studentId);
    }

    // ── Update student profile ───────────────────────────────────────
    // PUT /api/students/update/{studentId}
    // Body: { branch, cgpa, phoneNumber, skills, resumeLink }
    @PutMapping("/update/{studentId}")
    public Map<String, Object> updateProfile(@PathVariable int studentId,
                                             @RequestBody Student student) {
        return userService.updateStudentProfile(studentId, student);
    }
}