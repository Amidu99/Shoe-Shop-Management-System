package lk.ijse.HelloShoesBE.service;

import lk.ijse.HelloShoesBE.secureAndResponse.response.JwtAuthResponse;
import lk.ijse.HelloShoesBE.secureAndResponse.secure.SignIn;
import lk.ijse.HelloShoesBE.secureAndResponse.secure.SignUp;

public interface AuthenticationService {
    JwtAuthResponse signUp(SignUp newSignUp);
    JwtAuthResponse signIn(SignIn signIn);
    JwtAuthResponse refreshToken(String refreshToken);
}