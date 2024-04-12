package com.accountservice.controller;

import com.accountservice.domain.user.DataSaveUser;
import com.accountservice.domain.user.UserEntity;
import com.accountservice.domain.user.UserRepository;
import com.accountservice.infra.error.DataResponseSuccessfully;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @PostMapping
    public ResponseEntity<DataResponseSuccessfully> createUser(@RequestBody @Valid DataSaveUser dataSaveUser, UriComponentsBuilder uriComponentsBuilder) {
        UserEntity userEntity = userRepository.save(new UserEntity(dataSaveUser));

        URI uri = uriComponentsBuilder.path("/api/v1/users/{id}").buildAndExpand(userEntity.getUser_id()).toUri();

        return ResponseEntity.created(uri).body(new DataResponseSuccessfully(HttpStatus.CREATED, "User " + userEntity.getUsername() + "has been created successfully"));
    }
}
