package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public UserController(CustomerService customerService, EmployeeService employeeService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertCustomerDTOToEntity(customerDTO);
        return convertCustomerEntityToDTO(customerService.saveCustomer(customer));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customers.forEach(
                (customer) -> {
                    customerDTOList.add(convertCustomerEntityToDTO(customer));
                }
        );
        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return convertCustomerEntityToDTO(customerService.getOwnerByPet(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        return convertEmployeeEntityToDTO(employeeService.saveEmployee(employee));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEmployeeEntityToDTO(employeeService.getEmployee(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeService.getEmployee(employeeId);
        employee.setDaysAvailable(new ArrayList<>(daysAvailable));
        employeeService.saveEmployee(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        LocalDate serviceDate = employeeRequestDTO.getDate();
        List<Employee> employees = employeeService.findEmployeesForService(serviceDate.getDayOfWeek(), employeeRequestDTO.getSkills());
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employees.forEach((employee) -> {
            employeeDTOList.add(convertEmployeeEntityToDTO(employee));
        });
        return employeeDTOList;
    }

    private static CustomerDTO convertCustomerEntityToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        if (null != customer.getPets()) {
            List<Long> petIds = new ArrayList<>();
            customer.getPets().forEach((pet) -> {
                petIds.add(pet.getId());
            });
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private static Customer convertCustomerDTOToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        if (null != customerDTO.getPetIds()) {
            List<Pet> pets = new ArrayList<>();
            customerDTO.getPetIds().forEach((id) -> {
                Pet pet = new Pet();
                pet.setId(id);
                pets.add(pet);
            });
            customer.setPets(pets);
        }
        return customer;
    }

    private static EmployeeDTO convertEmployeeEntityToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        if (null != employee.getSkills()) {
            employeeDTO.setSkills(new TreeSet<>(employee.getSkills()));
        }
        if (null != employee.getDaysAvailable()) {
            employeeDTO.setDaysAvailable(new TreeSet<>(employee.getDaysAvailable()));
        }
        return employeeDTO;
    }

    private static Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        if (null != employeeDTO.getSkills()) {
            employee.setSkills(new ArrayList<>(employeeDTO.getSkills()));
        }
        if (null != employeeDTO.getDaysAvailable()) {
            employee.setDaysAvailable(new ArrayList<>(employeeDTO.getDaysAvailable()));
        }

        return employee;
    }

}
