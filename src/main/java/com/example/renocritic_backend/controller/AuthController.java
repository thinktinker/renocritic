package com.example.renocritic_backend.controller;

import com.example.renocritic_backend.dto.UserDTO;
import com.example.renocritic_backend.dto.LoginDTO;
import com.example.renocritic_backend.dto.RefreshTokenDTO;
import com.example.renocritic_backend.exception.RegistrationFailedException;
import com.example.renocritic_backend.exception.ResourceNotFoundException;
import com.example.renocritic_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/api")
// @CrossOrigin("*")   // // Any client can communicate with this controller
public class AuthController {

    @Autowired
    private AuthService authService;

    //register an account
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@Valid @RequestBody UserDTO signUpRequest) throws RegistrationFailedException {
        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    //sign in
    @PostMapping("/signin")
    public ResponseEntity<UserDTO> signIn(@Valid @RequestBody LoginDTO signInRequest) throws ResourceNotFoundException {
        return new ResponseEntity<>(authService.signIn(signInRequest), HttpStatus.OK);
    }

    //refresh token
    @PostMapping("/refreshtoken")
    public ResponseEntity<UserDTO> refreshToken(@Valid @RequestBody RefreshTokenDTO tokenRequest) throws ResourceNotFoundException{
        return new ResponseEntity<>(authService.refreshToken(tokenRequest), HttpStatus.OK);
    }

}