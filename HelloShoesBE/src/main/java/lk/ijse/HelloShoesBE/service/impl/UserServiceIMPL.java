package lk.ijse.HelloShoesBE.service.impl;

import lk.ijse.HelloShoesBE.dto.UserDTO;
import lk.ijse.HelloShoesBE.repo.UserRepo;
import lk.ijse.HelloShoesBE.service.UserService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService {
    private final UserRepo userRepo;
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

    @Override
    public List<UserDTO> getAllUsers() {
        return mapping.toUserDTOList(userRepo.findAll());
    }
}