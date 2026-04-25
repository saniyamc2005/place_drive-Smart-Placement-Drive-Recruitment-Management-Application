package com.example.placementdrive.service;

import com.example.placementdrive.model.*;
import com.example.placementdrive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ApplicationService {

    @Autowired private ApplicationRepository appRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private JobRepository jobRepo;

    // ── Student applies for a job ────────────────────────────────────
    public Map<String, Object> applyForJob(int studentId, int jobId) {
        Map<String, Object> res = new HashMap<>();

        Optional<Student> stuOpt = studentRepo.findById(studentId);
        Optional<Job>     jobOpt = jobRepo.findById(jobId);

        if (stuOpt.isEmpty()) {
            res.put("success", false); res.put("message", "Student not found"); return res;
        }
        if (jobOpt.isEmpty()) {
            res.put("success", false); res.put("message", "Job not found"); return res;
        }

        Student student = stuOpt.get();
        Job     job     = jobOpt.get();

        // Check job is still open
        if (!"Open".equals(job.getStatus())) {
            res.put("success", false);
            res.put("message", "This job is no longer accepting applications");
            return res;
        }

        // Check CGPA eligibility
        if (student.getCgpa() < job.getMinCgpa()) {
            res.put("success", false);
            res.put("message", "Not eligible. Minimum CGPA required: " + job.getMinCgpa()
                    + ". Your CGPA: " + student.getCgpa());
            return res;
        }

        // Check duplicate application
        if (appRepo.existsByStudentAndJob(student, job)) {
            res.put("success", false);
            res.put("message", "You have already applied for this job");
            return res;
        }

        Application app = new Application();
        app.setStudent(student);
        app.setJob(job);
        app.setStatus("Applied");
        app.setAppliedDate(LocalDate.now().toString());

        appRepo.save(app);
        res.put("success", true);
        res.put("message", "Applied successfully!");
        res.put("applicationId", app.getId());
        res.put("status", "Applied");
        res.put("appliedDate", app.getAppliedDate());
        return res;
    }

    // ── Get applications by student ──────────────────────────────────
    public Map<String, Object> getApplicationsByStudent(int studentId) {
        Map<String, Object> res = new HashMap<>();
        Optional<Student> stu = studentRepo.findById(studentId);
        if (stu.isEmpty()) {
            res.put("success", false); res.put("message", "Student not found"); return res;
        }
        List<Application> apps = appRepo.findByStudent(stu.get());
        res.put("success", true);
        res.put("applications", apps);
        res.put("count", apps.size());
        return res;
    }

    // ── Get applications by job (company views applicants) ───────────
    public Map<String, Object> getApplicationsByJob(int jobId) {
        Map<String, Object> res = new HashMap<>();
        Optional<Job> job = jobRepo.findById(jobId);
        if (job.isEmpty()) {
            res.put("success", false); res.put("message", "Job not found"); return res;
        }
        List<Application> apps = appRepo.findByJob(job.get());
        res.put("success", true);
        res.put("applications", apps);
        res.put("count", apps.size());
        return res;
    }

    // ── Update application status ────────────────────────────────────
    // Valid statuses: Applied / Shortlisted / Selected / Rejected
    public Map<String, Object> updateStatus(int appId, String status, String remarks) {
        Map<String, Object> res = new HashMap<>();

        List<String> validStatuses = Arrays.asList("Applied", "Shortlisted", "Selected", "Rejected");
        if (!validStatuses.contains(status)) {
            res.put("success", false);
            res.put("message", "Invalid status. Use: Applied / Shortlisted / Selected / Rejected");
            return res;
        }

        Optional<Application> opt = appRepo.findById(appId);
        if (opt.isEmpty()) {
            res.put("success", false); res.put("message", "Application not found"); return res;
        }

        Application app = opt.get();
        app.setStatus(status);
        if (remarks != null && !remarks.isEmpty()) app.setRemarks(remarks);
        appRepo.save(app);

        res.put("success", true);
        res.put("message", "Status updated to: " + status);
        res.put("applicationId", app.getId());
        res.put("status", app.getStatus());
        return res;
    }

    // ── Get all applications (admin) ─────────────────────────────────
    public Map<String, Object> getAllApplications() {
        Map<String, Object> res = new HashMap<>();
        List<Application> all = appRepo.findAll();
        res.put("success", true);
        res.put("applications", all);
        res.put("total", all.size());
        return res;
    }

    // ── Admin dashboard stats ────────────────────────────────────────
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> res = new HashMap<>();
        res.put("totalApplications",  appRepo.count());
        res.put("totalSelected",      appRepo.countByStatus("Selected"));
        res.put("totalShortlisted",   appRepo.countByStatus("Shortlisted"));
        res.put("totalRejected",      appRepo.countByStatus("Rejected"));
        res.put("totalPending",       appRepo.countByStatus("Applied"));
        res.put("totalJobs",          jobRepo.count());
        res.put("totalStudents",      studentRepo.count());
        return res;
    }

    // ── Withdraw / cancel an application ────────────────────────────
    public Map<String, Object> withdrawApplication(int appId, int studentId) {
        Map<String, Object> res = new HashMap<>();
        Optional<Application> opt = appRepo.findById(appId);
        if (opt.isEmpty()) {
            res.put("success", false); res.put("message", "Application not found"); return res;
        }
        Application app = opt.get();
        if (app.getStudent().getId() != studentId) {
            res.put("success", false); res.put("message", "Unauthorized"); return res;
        }
        if (!"Applied".equals(app.getStatus())) {
            res.put("success", false);
            res.put("message", "Cannot withdraw — application is already " + app.getStatus());
            return res;
        }
        appRepo.deleteById(appId);
        res.put("success", true);
        res.put("message", "Application withdrawn successfully");
        return res;
    }
}