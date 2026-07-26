package com.likelion.seminar.student.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private String studentId;     // 학번
    private String name;          // 이름
    private LocalDate dateOfBirth; // 생년월일
}