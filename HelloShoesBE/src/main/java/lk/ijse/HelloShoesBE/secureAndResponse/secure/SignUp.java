package lk.ijse.HelloShoesBE.secureAndResponse.secure;

import lk.ijse.HelloShoesBE.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUp {
    private String userCode;
    private String email;
    private String password;
    private Role role;
    private String employeeCode;
}