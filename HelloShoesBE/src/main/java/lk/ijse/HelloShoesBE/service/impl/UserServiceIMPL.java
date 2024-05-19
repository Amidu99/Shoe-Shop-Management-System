package lk.ijse.HelloShoesBE.service.impl;

import lk.ijse.HelloShoesBE.dto.UserDTO;
import lk.ijse.HelloShoesBE.repo.UserRepo;
import lk.ijse.HelloShoesBE.service.UserService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService {
    final private UserRepo userRepo;
    private final Mapping mapping;

    @Override
    public UserDetailsService userDetailsService() {
        return email ->
                userRepo.findByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("User Not Found!"));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return mapping.toUserDTO(userRepo.findUserByEmail(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
}