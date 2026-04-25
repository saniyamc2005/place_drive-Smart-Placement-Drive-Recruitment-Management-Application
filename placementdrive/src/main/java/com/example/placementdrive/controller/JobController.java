package com.example.placementdrive.controller;

import com.example.placementdrive.model.Job;
import com.example.placementdrive.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobService jobService;

    // ── Post a job ───────────────────────────────────────────────────
    // POST /api/jobs/post/{companyId}
    @PostMapping("/post/{companyId}")
    public Map<String, Object> postJob(@PathVariable int companyId,
                                       @RequestBody Job job) {
        return jobService.postJob(companyId, job);
    }

    // ── Get all open jobs (students browse) ──────────────────────────
    // GET /api/jobs/open
    @GetMapping("/open")
    public List<Job> getAllOpenJobs() {
        return jobService.getAllOpenJobs();
    }

    // ── Get all jobs (admin) ─────────────────────────────────────────
    // GET /api/jobs/all
    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    // ── Get jobs by company ──────────────────────────────────────────
    // GET /api/jobs/company/{companyId}
    @GetMapping("/company/{companyId}")
    public Map<String, Object> getByCompany(@PathVariable int companyId) {
        return jobService.getJobsByCompany(companyId);
    }

    // ── Get job by ID ────────────────────────────────────────────────
    // GET /api/jobs/{jobId}
    @GetMapping("/{jobId}")
    public Map<String, Object> getById(@PathVariable int jobId) {
        return jobService.getJobById(jobId);
    }

    // ── Update job ───────────────────────────────────────────────────
    // PUT /api/jobs/update/{jobId}
    @PutMapping("/update/{jobId}")
    public Map<String, Object> updateJob(@PathVariable int jobId,
                                         @RequestBody Job job) {
        return jobService.updateJob(jobId, job);
    }

    // ── Close job ────────────────────────────────────────────────────
    // PUT /api/jobs/close/{jobId}
    @PutMapping("/close/{jobId}")
    public Map<String, Object> closeJob(@PathVariable int jobId) {
        return jobService.closeJob(jobId);
    }

    // ── Delete job ───────────────────────────────────────────────────
    // DELETE /api/jobs/delete/{jobId}
    @DeleteMapping("/delete/{jobId}")
    public Map<String, Object> deleteJob(@PathVariable int jobId) {
        return jobService.deleteJob(jobId);
    }

    // ── Jobs eligible for a student based on CGPA ────────────────────
    // GET /api/jobs/eligible/{cgpa}
    @GetMapping("/eligible/{cgpa}")
    public List<Job> getEligibleJobs(@PathVariable double cgpa) {
        return jobService.getEligibleJobs(cgpa);
    }
}