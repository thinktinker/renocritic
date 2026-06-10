package com.example.renocritic_backend.service;

import com.example.renocritic_backend.dto.UserDTO;
import com.example.renocritic_backend.dto.LoginDTO;
import com.example.renocritic_backend.dto.RefreshTokenDTO;
import com.example.renocritic_backend.dto.UpdateUserDTO;
import com.example.renocritic_backend.exception.RegistrationFailedException;
import com.example.renocritic_backend.exception.ResourceNotFoundException;
import com.example.renocritic_backend.model.User;
import com.example.renocritic_backend.repository.UserRepository;
import com.example.renocritic_backend.utils.JWTUtils;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Builder
@Service
public class AuthService {

    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // signUp method
    public UserDTO signUp(UserDTO signupRequest) throws RegistrationFailedException{

        User user = User.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .email(signupRequest.getEmail())
                .phone(signupRequest.getPhone())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();

        User result = userRepository.save(user);

        if(result.getId() > 0){

            return UserDTO.builder()
                    .firstName(result.getFirstName())
                    .lastName(result.getLastName())
                    .email(result.getEmail())
                    .message("User saved successfully.")
                    .build();
            // option: generate a token for the end-user and access restricted pages immediately
            // .setToken(jwtUtils.generateToken(user))

        }

        throw new RegistrationFailedException("Failed to register the user.");
    }

    // signIn method
    public UserDTO signIn(LoginDTO signInRequest) throws ResourceNotFoundException{

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()));

        var user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("Invalid username or password."));

        var token = jwtUtils.generateToken(user);

        var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .token(token)
                .refreshToken(refreshToken)
                .message("Signed in successfully.")
                .expirationTime("24Hr")
                .build();
    }

    // refreshToken
    public UserDTO refreshToken(RefreshTokenDTO refreshTokenRequest) throws ResourceNotFoundException{

        var refreshToken = refreshTokenRequest.getRefreshToken();

        String userEmail = jwtUtils.extractUsername(refreshToken);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid username or password."));

        if(jwtUtils.isTokenValid(refreshToken, user)){

            var newToken = jwtUtils.generateToken(user);
            var newRefreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            return UserDTO.builder()
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .message("Refreshed token successfully.")
                    .expirationTime("168Hr")
                    .build();
        }

        return null;    // TODO
    }

    // returning userProfile - when the user views his/her profile
    public UserDTO profile(){

        // the bearer token is passed in and used by authentication
        // to extract the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var user = new User();
        user = userRepository.findByEmail(authentication.getName()).orElseThrow();

        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    // update userProfile
    public UserDTO updateProfile(UpdateUserDTO updateRequest){

        // the bearer token is passed in and used by authentication
        // to extract the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getName()).map(_user->{
            _user.setFirstName(updateRequest.getFirstName());
            _user.setLastName(updateRequest.getLastName());
            _user.setPhone(updateRequest.getPhone());
            _user.setEmail(updateRequest.getEmail());

            if(updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty())
                _user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

            return userRepository.save(_user);
        }).orElseThrow();

        var token = jwtUtils.generateToken(user);

        var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

        return UserDTO.builder()
                .message("Profile updated successfully")
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    // update userProfile by admin
    public UserDTO adminUpdateProfile(User currentUser, User updateRequest){

        User user = userRepository.findByEmail(currentUser.getEmail()).map(_user->{
            _user.setFirstName(updateRequest.getFirstName());
            _user.setLastName(updateRequest.getLastName());
            _user.setEmail(updateRequest.getEmail());
            _user.setRole(updateRequest.getRole());
            _user.setPhone(updateRequest.getPhone());
            _user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
            return userRepository.save(_user);
        }).orElseThrow();

        return UserDTO.builder()
                .message("Profile updated successfully.")
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .password(user.getPassword())
                .build();
    }
}
