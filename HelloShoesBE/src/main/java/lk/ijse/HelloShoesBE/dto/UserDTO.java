package lk.ijse.HelloShoesBE.dto;

import lk.ijse.HelloShoesBE.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO implements SuperDTO {
    private String userCode;
    private String email;
    private String password;
    private Role role;
    private String employeeCode;
}