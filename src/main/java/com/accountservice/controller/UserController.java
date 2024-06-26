package com.accountservice.controller;

import com.accountservice.domain.user.*;
import com.accountservice.domain.user.service.UpdateUserService;
import com.accountservice.infra.error.DataResponseError;
import com.accountservice.infra.error.DataResponseSuccessfully;
import com.accountservice.infra.error.IntegrityValidation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UpdateUserService updateUserService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DataResponseSuccessfully> createUser(@RequestBody @Valid DataSaveUser dataSaveUser, UriComponentsBuilder uriComponentsBuilder) {
        UserEntity userEntity = userRepository.save(new UserEntity(dataSaveUser, passwordEncoder.encode(dataSaveUser.password())));
        URI uri = uriComponentsBuilder.path("/api/v1/users/{id}").buildAndExpand(userEntity.getUser_id()).toUri();
        return ResponseEntity.created(uri).body(new DataResponseSuccessfully("201", "User " + userEntity.getUsername() + " has been created successfully"));

    }

    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DataResponseSuccessfully> updateUser(@RequestBody @Valid DataUpdateUser dataUpdateUser) {
        UserEntity userEntity = updateUserService.update(dataUpdateUser);

        return ResponseEntity.ok(new DataResponseSuccessfully("200", "User " + userEntity.getUsername() + " has been updated successfully"));
    }

    @GetMapping
    public ResponseEntity<Page<DataResponseUser>> listUsers(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable).map(DataResponseUser::new));
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<DataResponseUser> getUserData(@PathVariable UUID user_id) {
        UserEntity userEntity = userRepository.findById(user_id).get();

        return ResponseEntity.ok(new DataResponseUser(userEntity));
    }
}
