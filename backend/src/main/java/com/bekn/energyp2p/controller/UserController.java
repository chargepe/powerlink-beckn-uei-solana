package com.bekn.energyp2p.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bekn.energyp2p.dto.UserRegistrationRequest;
import com.bekn.energyp2p.dto.response.RegisterUserResponseDTO;
import com.bekn.energyp2p.dto.response.UserResponseDTO;
import com.bekn.energyp2p.models.Grid;
import com.bekn.energyp2p.models.User;
import com.bekn.energyp2p.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> registerUser(
            @RequestBody UserRegistrationRequest userRegistrationRequest) {
        RegisterUserResponseDTO registeredUser = userService.registerUser(userRegistrationRequest);
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.getUser(id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping("/checkIfUserExists/{email}")
    public ResponseEntity<UserResponseDTO> checkIfUserExsists(@PathVariable String email) {
        UserResponseDTO userResponseDTO = userService.checkIfUserExsists(email);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping("/transaction/{transactionString}")
    public ResponseEntity<JsonNode> getTransactionDetails(@PathVariable String transactionString) {
        JsonNode transactionDetails = userService.getTransactionDetails(transactionString);
        return ResponseEntity.ok(transactionDetails);
    }
}