package com.hop.drivesharing.hopapplication.rest.v1;

import com.hop.drivesharing.hopapplication.exception.AuthorizationResponseException;
import com.hop.drivesharing.hopapplication.rest.v1.dto.AuthenticationRequest;
import com.hop.drivesharing.hopapplication.rest.v1.dto.AuthenticationResponse;
import com.hop.drivesharing.hopapplication.security.auth.AuthenticationService;
import com.hop.drivesharing.hopapplication.security.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws AuthorizationResponseException {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) throws AuthorizationResponseException {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
