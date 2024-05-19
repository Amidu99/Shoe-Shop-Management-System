package lk.ijse.HelloShoesBE.secureAndResponse.secure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignIn {
    private String email;
    private String password;
}