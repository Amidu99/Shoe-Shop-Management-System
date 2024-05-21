package lk.ijse.HelloShoesBE.service.impl;

import lk.ijse.HelloShoesBE.dto.UserDTO;
import lk.ijse.HelloShoesBE.entity.User;
import lk.ijse.HelloShoesBE.repo.UserRepo;
import lk.ijse.HelloShoesBE.secureAndResponse.response.JwtAuthResponse;
import lk.ijse.HelloShoesBE.secureAndResponse.secure.SignIn;
import lk.ijse.HelloShoesBE.secureAndResponse.secure.SignUp;
import lk.ijse.HelloShoesBE.service.AuthenticationService;
import lk.ijse.HelloShoesBE.service.JwtService;
import lk.ijse.HelloShoesBE.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceIMPL implements AuthenticationService {
    private final Mapping mapping;
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthResponse signUp(SignUp newSignUp) {
        UserDTO userDTO = UserDTO.builder()
                .userCode(UUID.randomUUID().toString())
                .email(newSignUp.getEmail())
                .password(passwordEncoder.encode(newSignUp.getPassword()))
                .role(newSignUp.getRole())
                .employeeCode(newSignUp.getEmployeeCode())
                .build();
        User saveUser = userRepo.save(mapping.toUserEntity(userDTO));
        String generateToken = jwtService.generateToken(saveUser);
        return JwtAuthResponse.builder().token(generateToken).build();
    }

    @Override
    public JwtAuthResponse signIn(SignIn signIn) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.getEmail(),signIn.getPassword()));
        User user = userRepo.findByEmail(signIn.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        var generateToken = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(generateToken).build();
    }

    @Override
    public JwtAuthResponse refreshToken(String refreshToken) {
        var User = userRepo.findByEmail(jwtService.extractUserName(refreshToken)).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return JwtAuthResponse.builder().
                token(jwtService.generateToken(User)).build();
    }

    @Override
    public boolean matchPassword(UserDTO userDTO) {
        User existingUser = userRepo.findUserByEmail(userDTO.getEmail());
        return passwordEncoder.matches(userDTO.getUserCode(), existingUser.getPassword());
    }

    @Override
    public void updatePassword(UserDTO userDTO) {
        User existingUser = userRepo.findUserByEmail(userDTO.getEmail());
        String newPassword = passwordEncoder.encode(userDTO.getPassword());
        existingUser.setPassword(newPassword);
        userRepo.save(existingUser);
    }
}