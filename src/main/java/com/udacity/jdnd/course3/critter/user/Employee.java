package com.udacity.jdnd.course3.critter.user;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.List;

@Entity
public class Employee extends User {

    // See https://stackoverflow.com/questions/416208/jpa-map-collection-of-enums
    @ElementCollection(targetClass = EmployeeSkill.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "employee_skill")
    @Column(name = "skill")
    private List<EmployeeSkill> skills;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.ORDINAL)
    @CollectionTable(name = "employee_day")
    @Column(name = "day")
    private List<DayOfWeek> daysAvailable;

    public Employee() {

    }

    public Employee(List<EmployeeSkill> skills, List<DayOfWeek> daysAvailable) {
        this.skills = skills;
        this.daysAvailable = daysAvailable;
    }

    public Employee(Long id, String name, List<EmployeeSkill> skills, List<DayOfWeek> daysAvailable) {
        super(id, name);
        this.skills = skills;
        this.daysAvailable = daysAvailable;
    }

    public Employee(String name, List<EmployeeSkill> skills, List<DayOfWeek> daysAvailable) {
        super(name);
        this.skills = skills;
        this.daysAvailable = daysAvailable;
    }

    public List<EmployeeSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<EmployeeSkill> skills) {
        this.skills = skills;
    }

    public List<DayOfWeek> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(List<DayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }
}
