package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {

    List<Schedule> findSchedulesByPetsIds(Long petId);

    List<Schedule> findSchedulesByEmployeesId(Long employeeId);

    List<Schedule> findSchedulesByPetsOwnerId(Long ownerId);
}
