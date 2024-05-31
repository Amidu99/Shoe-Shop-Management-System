package lk.ijse.HelloShoesBE.controller;

import jakarta.annotation.security.RolesAllowed;
import lk.ijse.HelloShoesBE.dto.UserDTO;
import lk.ijse.HelloShoesBE.service.AuthenticationService;
import lk.ijse.HelloShoesBE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class User {
    final static Logger logger = LoggerFactory.getLogger(User.class);
    private final UserService userService;
    private final AuthenticationService authService;

    @GetMapping("/health")
    @RolesAllowed({"ADMIN", "USER"})
    public String healthTest(){
        logger.info("User Health Test Passed.");
        return "User Health Test Passed.";
    }

    @GetMapping("/get")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getUserByEmail(@RequestHeader String email){
        boolean isExists = userService.existsByEmail(email);
        if (!isExists){
            logger.info("Not Exists User.");
            return ResponseEntity.noContent().build();
        }
        UserDTO userDTO = userService.getUserByEmail(email);
        logger.info("User founded");
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/getAll")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<?> getAllUsers(){
        List<UserDTO> allUsers = userService.getAllUsers();
        logger.info("No of all users: "+allUsers.size());
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
                    logger.info("Password changed successfully.");
                    return ResponseEntity.ok().build();
                }
                logger.info("Password didn't match!");
                return ResponseEntity.status(205).build();
            }
            logger.info("Not Exists User!");
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
            logger.info("Not Exists User.");
            return ResponseEntity.badRequest().body("User not found!");
        }
        userService.deleteUser(email);
        logger.info("User deleted.");
        return ResponseEntity.ok().build();
    }
}