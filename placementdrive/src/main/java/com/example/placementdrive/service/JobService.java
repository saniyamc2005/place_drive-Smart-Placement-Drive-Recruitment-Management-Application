package com.example.placementdrive.service;

import com.example.placementdrive.model.*;
import com.example.placementdrive.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobService {

    @Autowired private JobRepository jobRepo;
    @Autowired private CompanyRepository companyRepo;

    // ── Post a new job ───────────────────────────────────────────────
    public Map<String, Object> postJob(int companyId, Job job) {
        Map<String, Object> res = new HashMap<>();
        Optional<Company> compOpt = companyRepo.findById(companyId);
        if (compOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Company not found");
            return res;
        }
        job.setCompany(compOpt.get());
        job.setStatus("Open");
        Job saved = jobRepo.save(job);
        res.put("success", true);
        res.put("message", "Job posted successfully");
        res.put("job", saved);
        return res;
    }

    // ── Get all open jobs (for students) ─────────────────────────────
    public List<Job> getAllOpenJobs() {
        return jobRepo.findByStatus("Open");
    }

    // ── Get all jobs (admin) ─────────────────────────────────────────
    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    // ── Get jobs by company ──────────────────────────────────────────
    public Map<String, Object> getJobsByCompany(int companyId) {
        Map<String, Object> res = new HashMap<>();
        Optional<Company> comp = companyRepo.findById(companyId);
        if (comp.isEmpty()) {
            res.put("success", false);
            res.put("message", "Company not found");
            return res;
        }
        List<Job> jobs = jobRepo.findByCompany(comp.get());
        res.put("success", true);
        res.put("jobs", jobs);
        res.put("count", jobs.size());
        return res;
    }

    // ── Get job by ID ────────────────────────────────────────────────
    public Map<String, Object> getJobById(int jobId) {
        Map<String, Object> res = new HashMap<>();
        Optional<Job> job = jobRepo.findById(jobId);
        if (job.isEmpty()) {
            res.put("success", false);
            res.put("message", "Job not found");
            return res;
        }
        res.put("success", true);
        res.put("job", job.get());
        return res;
    }

    // ── Update job ───────────────────────────────────────────────────
    public Map<String, Object> updateJob(int jobId, Job updated) {
        Map<String, Object> res = new HashMap<>();
        Optional<Job> opt = jobRepo.findById(jobId);
        if (opt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Job not found");
            return res;
        }
        Job job = opt.get();
        if (updated.getRole() != null)           job.setRole(updated.getRole());
        if (updated.getPackageAmount() > 0)      job.setPackageAmount(updated.getPackageAmount());
        if (updated.getMinCgpa() > 0)            job.setMinCgpa(updated.getMinCgpa());
        if (updated.getDeadline() != null)       job.setDeadline(updated.getDeadline());
        if (updated.getDescription() != null)    job.setDescription(updated.getDescription());
        if (updated.getLocation() != null)       job.setLocation(updated.getLocation());
        if (updated.getJobType() != null)        job.setJobType(updated.getJobType());
        if (updated.getRequiredSkills() != null) job.setRequiredSkills(updated.getRequiredSkills());
        if (updated.getOpenings() > 0)           job.setOpenings(updated.getOpenings());
        jobRepo.save(job);
        res.put("success", true);
        res.put("message", "Job updated successfully");
        res.put("job", job);
        return res;
    }

    // ── Close a job ──────────────────────────────────────────────────
    public Map<String, Object> closeJob(int jobId) {
        Map<String, Object> res = new HashMap<>();
        Optional<Job> opt = jobRepo.findById(jobId);
        if (opt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Job not found");
            return res;
        }
        Job job = opt.get();
        job.setStatus("Closed");
        jobRepo.save(job);
        res.put("success", true);
        res.put("message", "Job closed");
        return res;
    }

    // ── Delete job ───────────────────────────────────────────────────
    public Map<String, Object> deleteJob(int jobId) {
        Map<String, Object> res = new HashMap<>();
        if (!jobRepo.existsById(jobId)) {
            res.put("success", false);
            res.put("message", "Job not found");
            return res;
        }
        jobRepo.deleteById(jobId);
        res.put("success", true);
        res.put("message", "Job deleted successfully");
        return res;
    }

    // ── Filter eligible jobs for a student ──────────────────────────
    public List<Job> getEligibleJobs(double cgpa) {
        return jobRepo.findByMinCgpaLessThanEqual(cgpa);
    }
}