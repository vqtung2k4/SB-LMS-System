package com.edu.fsa.LmsSpringBoot.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "instructors")
public class Instructor {
    @Id
    @Column(name = "instructor_id")
    private String instructorId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "department")
    private String department;

    @Column(name = "title")
    private String title;

    @Column(name = "salary")
    private BigDecimal salary;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses;

    // Constructors
    public Instructor() {}

    public Instructor(String instructorId, String userId, String department, String title) {
        this.instructorId = instructorId;
        this.userId = userId;
        this.department = department;
        this.title = title;
    }


    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }
}
