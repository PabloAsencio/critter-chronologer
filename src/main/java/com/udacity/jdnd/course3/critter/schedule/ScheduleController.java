package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final PetService petService;
    private final EmployeeService employeeService;

    public ScheduleController(ScheduleService scheduleService, PetService petService, EmployeeService employeeService) {
        this.scheduleService = scheduleService;
        this.petService = petService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertScheduleDTOtoEntity(scheduleDTO);
        Schedule savedSchedule = saveSchedule(schedule);
        return convertScheduleEntityToDTO(savedSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return convertScheduleEntityToDTOList(schedules);
    }



    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.getSchedulesForPet(petId);
        return convertScheduleEntityToDTOList(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);
        return convertScheduleEntityToDTOList(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = scheduleService.getScheduleForCustomer(customerId);
        return convertScheduleEntityToDTOList(schedules);
    }

    private static ScheduleDTO convertScheduleEntityToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        if (null != schedule.getPets()) {
            List<Long> petIds = new ArrayList<>();
            schedule.getPets().forEach((pet -> {
                petIds.add(pet.getId());
            }));
            scheduleDTO.setPetIds(petIds);
        }
        if (null != schedule.getEmployees()) {
            List<Long> employeeIds = new ArrayList<>();
            schedule.getEmployees().forEach((employee -> {
                employeeIds.add(employee.getId());
            }));
            scheduleDTO.setEmployeeIds(employeeIds);
        }
        if (null != schedule.getActivities()) {
            scheduleDTO.setActivities(new TreeSet<>(schedule.getActivities()));
        }

        return scheduleDTO;
    }

    private List<ScheduleDTO> convertScheduleEntityToDTOList(List<Schedule> schedules) {
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        schedules.forEach(schedule -> {
            scheduleDTOList.add(convertScheduleEntityToDTO(schedule));
        });
        return scheduleDTOList;
    }

    private static Schedule convertScheduleDTOtoEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setId(scheduleDTO.getId());
        schedule.setDate(scheduleDTO.getDate());
        if (null != scheduleDTO.getActivities()) {
            schedule.setActivities(new ArrayList<>(scheduleDTO.getActivities()));
        }
        if (null != scheduleDTO.getPetIds()) {
            List<Pet> pets = new ArrayList<>();
            scheduleDTO.getPetIds().forEach((id) -> {
                Pet pet = new Pet();
                pet.setId(id);
                pets.add(pet);
            });
            schedule.setPets(pets);
        }
        if (null != scheduleDTO.getEmployeeIds()) {
            List<Employee> employees = new ArrayList<>();
            scheduleDTO.getEmployeeIds().forEach((id) -> {
                Employee employee = new Employee();
                employee.setId(id);
                employees.add(employee);
            });
            schedule.setEmployees(employees);
        }


        return schedule;
    }

    private Schedule saveSchedule(Schedule schedule) {
        List<Pet> pets = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        schedule.getPets().forEach((pet -> {
            Pet newPet = petService.getPetById(pet.getId());
            pets.add(newPet);
        }));
        schedule.getEmployees().forEach((employee -> {
            Employee newEmployee = employeeService.getEmployee(employee.getId());
            employees.add(newEmployee);
        }));
        schedule.setPets(pets);
        schedule.setEmployees(employees);
        Schedule savedSchedule = scheduleService.saveSchedule(schedule);
        pets.forEach((pet) -> {
            if (null != pet.getSchedules()) {
                pet.getSchedules().add(savedSchedule);
            } else {
                List<Schedule> schedules = new ArrayList<>();
                schedules.add(savedSchedule);
                pet.setSchedules(schedules);
            }
        });
        employees.forEach((employee) -> {
            if (null != employee.getSchedules()) {
                employee.getSchedules().add(savedSchedule);
            } else {
                List<Schedule> schedules = new ArrayList<>();
                schedules.add(savedSchedule);
                employee.setSchedules(schedules);
            }
        });
        return savedSchedule;
    }
}
