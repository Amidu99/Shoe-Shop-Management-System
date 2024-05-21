package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.UserDTO;
import lk.ijse.HelloShoesBE.service.AuthenticationService;
import lk.ijse.HelloShoesBE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class User {
    private final UserService userService;
    private final AuthenticationService authService;

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

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> updateUserPassword(@RequestBody UserDTO userDTO) {
        try {
            if (userService.existsByEmail(userDTO.getEmail())) {
                if(authService.matchPassword(userDTO)) {
                    authService.updatePassword(userDTO);
                    System.out.println("Password changed successfully.");
                    return ResponseEntity.ok().build();
                }
                System.out.println("Password didn't match!");
                return ResponseEntity.status(205).build();
            }
            System.out.println("Not Exists User!");
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteUser(@RequestHeader String email){
        boolean isExists = userService.existsByEmail(email);
        if (!isExists){
            System.out.println("Not Exists User.");
            return ResponseEntity.badRequest().body("User not found!");
        }
        userService.deleteUser(email);
        System.out.println("User deleted.");
        return ResponseEntity.ok().build();
    }
}