package com.example.renocritic_backend.controller;

import com.example.renocritic_backend.dto.QuoteDTO;
import com.example.renocritic_backend.dto.UpdateUserDTO;
import com.example.renocritic_backend.exception.ResourceNotFoundException;
import com.example.renocritic_backend.model.Quote;
import com.example.renocritic_backend.model.User;
import com.example.renocritic_backend.repository.QuoteRepository;
import com.example.renocritic_backend.repository.UserRepository;
import com.example.renocritic_backend.service.AuthService;
import com.example.renocritic_backend.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restricted")
// @CrossOrigin("*")   // Any client can communicate with this controller
public class RestrictedController {

    @Autowired
    private AuthService authService;

    @Autowired
    private QuoteService quoteService;

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(){
        return new ResponseEntity<>(authService.profile(), HttpStatus.OK);
    }

    @PutMapping("/updateprofile")
    public ResponseEntity<Object> updateProfile(@Valid @RequestBody UpdateUserDTO updateProfileRequest){
        return new ResponseEntity<>(authService.updateProfile(updateProfileRequest), HttpStatus.OK);
    }

    @PostMapping("/savequote")
    public ResponseEntity<Object> savequote(@Valid @RequestBody List<QuoteDTO> QuoteDTOS) throws ResourceNotFoundException{
        return new ResponseEntity<>( quoteService.saveAll(QuoteDTOS), HttpStatus.OK);
    }

    @GetMapping("/finduserquotes")
    public ResponseEntity<Object> findUserQuotes() throws ResourceNotFoundException{
        return new ResponseEntity<>(quoteService.findUserQuotes(), HttpStatus.OK);
    }

}