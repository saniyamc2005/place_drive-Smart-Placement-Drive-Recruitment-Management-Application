package com.example.placementdrive.controller;

import com.example.placementdrive.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService appService;

    // ── Student applies for a job ────────────────────────────────────
    // POST /api/applications/apply/{studentId}/{jobId}
    @PostMapping("/apply/{studentId}/{jobId}")
    public Map<String, Object> apply(@PathVariable int studentId,
                                     @PathVariable int jobId) {
        return appService.applyForJob(studentId, jobId);
    }

    // ── Student views own applications ───────────────────────────────
    // GET /api/applications/student/{studentId}
    @GetMapping("/student/{studentId}")
    public Map<String, Object> getByStudent(@PathVariable int studentId) {
        return appService.getApplicationsByStudent(studentId);
    }

    // ── Company views applicants for a job ───────────────────────────
    // GET /api/applications/job/{jobId}
    @GetMapping("/job/{jobId}")
    public Map<String, Object> getByJob(@PathVariable int jobId) {
        return appService.getApplicationsByJob(jobId);
    }

    // ── Update application status ────────────────────────────────────
    // PUT /api/applications/status/{applicationId}
    // Body: { "status": "Shortlisted", "remarks": "Good profile" }
    @PutMapping("/status/{applicationId}")
    public Map<String, Object> updateStatus(@PathVariable int applicationId,
                                            @RequestBody Map<String, String> body) {
        String status  = body.get("status");
        String remarks = body.getOrDefault("remarks", "");
        return appService.updateStatus(applicationId, status, remarks);
    }

    // ── Admin: get all applications ──────────────────────────────────
    // GET /api/applications/all
    @GetMapping("/all")
    public Map<String, Object> getAll() {
        return appService.getAllApplications();
    }

    // ── Admin: dashboard statistics ──────────────────────────────────
    // GET /api/applications/stats
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return appService.getDashboardStats();
    }

    // ── Student withdraws application ────────────────────────────────
    // DELETE /api/applications/withdraw/{applicationId}/{studentId}
    @DeleteMapping("/withdraw/{applicationId}/{studentId}")
    public Map<String, Object> withdraw(@PathVariable int applicationId,
                                        @PathVariable int studentId) {
        return appService.withdrawApplication(applicationId, studentId);
    }
}