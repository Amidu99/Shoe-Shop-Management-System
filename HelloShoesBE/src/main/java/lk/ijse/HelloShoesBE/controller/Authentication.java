package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.dto.EmployeeDTO;
import lk.ijse.HelloShoesBE.secureAndResponse.response.JwtAuthResponse;
import lk.ijse.HelloShoesBE.secureAndResponse.secure.SignIn;
import lk.ijse.HelloShoesBE.secureAndResponse.secure.SignUp;
import lk.ijse.HelloShoesBE.service.AuthenticationService;
import lk.ijse.HelloShoesBE.service.EmployeeService;
import lk.ijse.HelloShoesBE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor
public class Authentication {
    private final AuthenticationService authenticationService;
    private final EmployeeService employeeService;
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<JwtAuthResponse> signup(@RequestBody SignUp signup) {
        boolean isExistsEmployee = employeeService.existsByEmail(signup.getEmail());
        boolean isExistsUser = userService.existsByEmail(signup.getEmail());

        if (!isExistsEmployee) {
            System.out.println("Not Exists Employee.");
            return ResponseEntity.status(204).build();
        }

        if (isExistsUser) {
            System.out.println("Already Exists User.");
            return ResponseEntity.status(205).build();
        }

        EmployeeDTO employeeDTO = employeeService.getEmployeeByEmail(signup.getEmail());
        SignUp newSignUp = new SignUp("", signup.getEmail(), signup.getPassword(), employeeDTO.getRole(), employeeDTO.getEmployeeCode());
        return ResponseEntity.ok(authenticationService.signUp(newSignUp));
    }

    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthResponse> signIn( @RequestBody SignIn signIn ) {
        return ResponseEntity.ok(authenticationService.signIn(signIn));
    }

    @GetMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh( @RequestParam ("refreshToken") String refreshToken ) {
        System.out.println(refreshToken);
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }
}