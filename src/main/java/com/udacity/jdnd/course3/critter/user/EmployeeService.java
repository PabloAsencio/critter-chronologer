package com.udacity.jdnd.course3.critter.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(RuntimeException::new);
    }

    public void setEmployeeAvailability(Long employeeId, Collection<DayOfWeek> availability) {
        Employee employee = getEmployee(employeeId);
        employee.setDaysAvailable(new ArrayList<>(availability));
        saveEmployee(employee);
    }

    public List<Employee> findEmployeesForService(DayOfWeek day, Collection<EmployeeSkill> skills) {
        return employeeRepository.findEmployeesByDaysAvailableContainsAndSkillsIn(day, skills);
    }
}
