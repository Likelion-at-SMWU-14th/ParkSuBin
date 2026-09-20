package com.likelion.seminar.global.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("존재하지 않는 사용자입니다. id: " + id);
    }
}
