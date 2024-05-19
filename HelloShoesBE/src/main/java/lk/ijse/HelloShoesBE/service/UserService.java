package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    UserDTO getUserByEmail(String email);
    boolean existsByEmail(String email);
}