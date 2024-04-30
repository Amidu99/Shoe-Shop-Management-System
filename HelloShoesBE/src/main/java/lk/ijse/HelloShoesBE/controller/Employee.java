package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.CustomerDTO;
import lk.ijse.HelloShoesBE.dto.EmployeeDTO;
import lk.ijse.HelloShoesBE.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class Employee {
    private final EmployeeService employeeService;

    @GetMapping("/health")
    public String healthTest(){
        System.out.println("Employee Health Test Passed.");
        return "Employee Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            validateEmployee(employeeDTO);
            if (employeeService.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
                System.out.println("Exists Employee.");
                return ResponseEntity.badRequest().body("This employee already exists.");
            }
            employeeService.saveEmployee(employeeDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getOneEmployee(@RequestHeader String employeeCode){
        boolean isExists = employeeService.existsByEmployeeCode(employeeCode);
        if (!isExists){
            System.out.println("Not Exists Employee.");
            return ResponseEntity.noContent().build();
        }
        EmployeeDTO employeeDTO = employeeService.getEmployeeByEmployeeCode(employeeCode);
        System.out.println("Employee founded: "+employeeDTO);
        return ResponseEntity.ok(employeeDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEmployees(){
        List<EmployeeDTO> allEmployees = employeeService.getAllEmployees();
        System.out.println("No of all employees: "+allEmployees.size());
        if (allEmployees.size() == 0) return ResponseEntity.ok().body("No employees found");
        return ResponseEntity.ok().body(allEmployees);
    }

    private void validateEmployee(EmployeeDTO employeeDTO) {
        if (!Pattern.compile("^[E]-\\d{4}$").matcher(employeeDTO.getEmployeeCode()).matches()) {
            throw new RuntimeException("Invalid Employee Code.");
        }
        if (!Pattern.compile("^[A-Za-z\\s]{3,}$").matcher(employeeDTO.getEmployeeName()).matches()) {
            throw new RuntimeException("Invalid Employee Name.");
        }
        if (!Pattern.compile("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$").matcher(employeeDTO.getContactNo()).matches()) {
            throw new RuntimeException("Invalid Employee Contact Number.");
        }
        System.out.println("Employee validated.");
    }
}