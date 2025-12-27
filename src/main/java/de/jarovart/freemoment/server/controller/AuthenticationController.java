/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.LoginDTO;
import de.jarovart.freemoment.server.model.dtos.requests.LoginRequest;
import de.jarovart.freemoment.server.model.dtos.requests.RegisterRequest;
import de.jarovart.freemoment.server.model.dtos.requests.ResetPasswordRequest;
import de.jarovart.freemoment.server.model.dtos.requests.SendEmailRequest;
import de.jarovart.freemoment.server.model.dtos.requests.VerifyTokenRequest;
import de.jarovart.freemoment.server.services.controllerservices.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Artem
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // wichtig für Flutter
public class AuthenticationController {

    private static final Logger log =
            LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody LoginRequest loginRequest) {
        log.info("POST /api/auth/login: for {}", loginRequest.username());
        LoginDTO loginDTO = authenticationService.getLoginToken(loginRequest);
        return ResponseEntity.ok().body(loginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        log.info("POST /api/auth/register: {}", registerRequest.email());
        authenticationService.registerPendingUser(registerRequest);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestBody VerifyTokenRequest verifyTokenRequest) {
        log.info("POST /api/auth/verify token");
        authenticationService.transferPendingUserToAppUser(verifyTokenRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resend(@RequestBody SendEmailRequest sendEmailRequest) {
        log.info("POST /api/auth/resend-verification: for {}", sendEmailRequest.email());
        authenticationService.updateTokenOfPendingUserAndResendEmail(sendEmailRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody SendEmailRequest sendEmailRequest) {
        log.info("POST /api/auth/forgot-password: for {}", sendEmailRequest.email());
        authenticationService.requestResetPassword(sendEmailRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.info("POST /api/auth/reset-password");
        authenticationService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok().build();
    }
}

