package com.likelion.seminar.dto;

import com.likelion.seminar.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final Integer age;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.age = user.getAge();
    }
}
