package com.likelion.seminar.student.service;

import com.likelion.seminar.student.dto.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {


    private final List<StudentDTO> studentDTOList = new ArrayList<>();

    // 1. 학생 등록 (POST)
    public void createStudent(StudentDTO studentDTO) {
        this.studentDTOList.add(studentDTO);
    }

    // 2. 전체 학생 목록 조회 (GET)
    public List<StudentDTO> getStudents() {
        return this.studentDTOList;
    }

    // 3. 특정 학생 정보 조회 (GET)
    public StudentDTO getStudentById(String studentId) {
        for (StudentDTO student : studentDTOList) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    // 4. 특정 학생 정보 수정 (PUT)
    public void updateStudent(String studentId, StudentDTO studentDTO) {
        for (StudentDTO student : studentDTOList) {
            if (student.getStudentId().equals(studentId)) {
                if (studentDTO.getName() != null) {
                    student.setName(studentDTO.getName());
                }
                if (studentDTO.getDateOfBirth() != null) {
                    student.setDateOfBirth(studentDTO.getDateOfBirth());
                }
                break;
            }
        }
    }

    // 5. 특정 학생 정보 삭제 (DELETE)
    public void deleteStudent(String studentId) {
        studentDTOList.removeIf(student -> student.getStudentId().equals(studentId));
    }
}