package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.UserDTO;
import lk.ijse.HelloShoesBE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class User {
    private final UserService userService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        System.out.println("User Health Test Passed.");
        return "User Health Test Passed.";
    }

    @GetMapping("/get")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getUserByEmail(@RequestHeader String email){
        boolean isExists = userService.existsByEmail(email);
        if (!isExists){
            System.out.println("Not Exists User.");
            return ResponseEntity.noContent().build();
        }
        UserDTO userDTO = userService.getUserByEmail(email);
        System.out.println("User founded: "+userDTO);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllUsers(){
        List<UserDTO> allUsers = userService.getAllUsers();
        System.out.println("No of all users: "+allUsers.size());
        if (allUsers.size() == 0) return ResponseEntity.ok().body("No users found");
        return ResponseEntity.ok().body(allUsers);
    }
}