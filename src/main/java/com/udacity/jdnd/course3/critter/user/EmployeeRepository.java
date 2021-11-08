package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findEmployeesByDaysAvailableContainsAndSkillsIn(DayOfWeek dayOfWeek, Collection<EmployeeSkill> skills);
}
