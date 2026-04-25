package com.example.placementdrive.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String role;
    private double packageAmount;      // in LPA
    private double minCgpa;            // minimum CGPA required
    private String deadline;           // format: YYYY-MM-DD
    private String description;
    private String location;
    private String jobType;            // Full-time / Internship / Contract
    private String requiredSkills;     // comma-separated
    private String status;             // Open / Closed
    private int openings;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // ─── Getters & Setters ───────────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public double getPackageAmount() { return packageAmount; }
    public void setPackageAmount(double packageAmount) { this.packageAmount = packageAmount; }

    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getOpenings() { return openings; }
    public void setOpenings(int openings) { this.openings = openings; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}