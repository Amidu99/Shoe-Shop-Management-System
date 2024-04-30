package lk.ijse.HelloShoesBE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "employee")
@Entity
public class Employee implements SuperEntity{
    @Id
    private String employeeCode;
    private String employeeName;
    private String employeePic;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String status;
    private String designation;
    @Enumerated(EnumType.STRING)
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