package lk.ijse.HelloShoesBE.dto;

import lk.ijse.HelloShoesBE.entity.Gender;
import lk.ijse.HelloShoesBE.entity.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO implements SuperDTO {
    private String customerCode;
    private String customerName;
    private Gender gender;
    private Date joinDate;
    private Level level;
    private int totalPoints;
    private Date dob;
    private String addLine1;
    private String addLine2;
    private String addLine3;
    private String addLine4;
    private String addLine5;
    private String contactNo;
    private String email;
    private Timestamp rpDateTime;
}