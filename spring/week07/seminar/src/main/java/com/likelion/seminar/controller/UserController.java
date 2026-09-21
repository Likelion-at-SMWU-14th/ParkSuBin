package com.likelion.seminar.controller;

import com.likelion.seminar.dto.UserResponse;
import com.likelion.seminar.dto.UserSaveRequest;
import com.likelion.seminar.dto.UserUpdateRequest;
import com.likelion.seminar.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public void saveUser(@Valid @RequestBody UserSaveRequest request) {
        userService.saveUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return userService.updateUser(id, request);
    }
}