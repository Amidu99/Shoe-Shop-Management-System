package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.EmployeeDTO;
import lk.ijse.HelloShoesBE.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class Employee {
    final static Logger logger = LoggerFactory.getLogger(Employee.class);
    private final EmployeeService employeeService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        logger.info("Employee Health Test Passed.");
        return "Employee Health Test Passed.";
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            validateEmployee(employeeDTO);
            if (employeeService.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
                logger.info("Exists Employee.");
                return ResponseEntity.badRequest().body("This employee already exists.");
            }
            employeeService.saveEmployee(employeeDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getOneEmployee(@RequestHeader String employeeCode){
        boolean isExists = employeeService.existsByEmployeeCode(employeeCode);
        if (!isExists){
            logger.info("Not Exists Employee.");
            return ResponseEntity.noContent().build();
        }
        EmployeeDTO employeeDTO = employeeService.getEmployeeByEmployeeCode(employeeCode);
        return ResponseEntity.ok(employeeDTO);
    }

    @GetMapping("/getByEmail")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getEmployeeByEmail(@RequestHeader String email){
        boolean isExists = employeeService.existsByEmail(email);
        if (!isExists){
            logger.info("Not Exists Employee.");
            return ResponseEntity.noContent().build();
        }
        EmployeeDTO employeeDTO = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.ok(employeeDTO);
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllEmployees(){
        List<EmployeeDTO> allEmployees = employeeService.getAllEmployees();
        logger.info("No of all employees: "+allEmployees.size());
        if (allEmployees.size() == 0) return ResponseEntity.ok().body("No employees found");
        return ResponseEntity.ok().body(allEmployees);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            validateEmployee(employeeDTO);
            if (employeeService.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
                employeeService.updateEmployee(employeeDTO);
                return ResponseEntity.ok().build();
            }
            logger.info("Not Exists Employee.");
            return ResponseEntity.badRequest().body("This employee not exists.");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteEmployee(@RequestHeader String employeeCode){
        boolean isExists = employeeService.existsByEmployeeCode(employeeCode);
        if (!isExists){
            logger.info("Not Exists Employee.");
            return ResponseEntity.badRequest().body("Employee not found!");
        }
        employeeService.deleteEmployee(employeeCode);
        logger.info("Employee deleted.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getNextCode")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getNextEmployeeCode(){
        String lastEmployeeCode = employeeService.getLastEmployeeCode();
        logger.info("Last EmployeeCode: "+lastEmployeeCode);
        if (lastEmployeeCode==null) return ResponseEntity.ok("E-0001");
        int nextCode = Integer.parseInt(lastEmployeeCode.replace("E-", "")) + 1;
        logger.info("Next EmployeeCode: "+nextCode);
        return ResponseEntity.ok(String.format("E-%04d", nextCode));
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
        logger.info("Employee validated.");
    }
}