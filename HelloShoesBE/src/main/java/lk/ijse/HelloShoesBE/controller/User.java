package lk.ijse.HelloShoesBE.controller;

import lk.ijse.HelloShoesBE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class User {
    private final UserService userService;

}