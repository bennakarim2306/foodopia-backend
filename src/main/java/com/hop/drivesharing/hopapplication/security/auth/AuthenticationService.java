package com.hop.drivesharing.hopapplication.security.auth;

import com.hop.drivesharing.hopapplication.exception.AuthorizationResponseException;
import com.hop.drivesharing.hopapplication.exception.UnregisteredUserException;
import com.hop.drivesharing.hopapplication.exception.UserAlreadyExistingException;
import com.hop.drivesharing.hopapplication.rest.v1.dto.AuthenticationRequest;
import com.hop.drivesharing.hopapplication.rest.v1.dto.AuthenticationResponse;
import com.hop.drivesharing.hopapplication.rest.v1.errorHandling.ErrorCode;
import com.hop.drivesharing.hopapplication.security.JwtService;
import com.hop.drivesharing.hopapplication.data.user.Role;
import com.hop.drivesharing.hopapplication.data.user.User;
import com.hop.drivesharing.hopapplication.data.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.hop.drivesharing.hopapplication.rest.v1.errorHandling.ErrorCode.UNREGISTERED_USER;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws UserAlreadyExistingException {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            String message = "User already existing with the email address provided" + user.getEmail();
            throw new UserAlreadyExistingException(message, ErrorCode.USER_ALREADY_REGISTERED);
        }


        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthorizationResponseException, AuthenticationException {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UnregisteredUserException("User unregistered with the email address: " + request.getEmail(), UNREGISTERED_USER));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
