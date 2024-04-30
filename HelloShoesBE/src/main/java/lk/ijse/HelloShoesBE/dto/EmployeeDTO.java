package lk.ijse.HelloShoesBE.dto;

import lk.ijse.HelloShoesBE.entity.Gender;
import lk.ijse.HelloShoesBE.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDTO implements SuperDTO {
    private String employeeCode;
    private String employeeName;
    private String employeePic;
    private Gender gender;
    private String status;
    private String designation;
    private Role role;
    private Date dob;
    private Date joinDate;
    private String branch;
    private String addLine1;
    private String addLine2;
    private String addLine3;
    private String addLine4;
    private String addLine5;
    private String contactNo;
    private String email;
    private String  guardian;
    private String  guardianContactNo;
}