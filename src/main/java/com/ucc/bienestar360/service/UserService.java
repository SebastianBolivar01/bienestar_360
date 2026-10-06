package com.ucc.bienestar360.service;

import com.ucc.bienestar360.dto.LoginRequest;
import com.ucc.bienestar360.dto.LoginResponse;
import com.ucc.bienestar360.dto.StudentProgressDto;
import com.ucc.bienestar360.model.Enrollment;
import com.ucc.bienestar360.model.Role;
import com.ucc.bienestar360.model.User;
import com.ucc.bienestar360.repository.AttendanceRepository;
import com.ucc.bienestar360.repository.EnrollmentRepository;
import com.ucc.bienestar360.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Simple validation for prototype/demo
            if (user.getPassword().equals(request.getPassword())) {
                return LoginResponse.builder()
                        .success(true)
                        .message("Inicio de sesión exitoso como " + user.getRole())
                        .user(user)
                        .token("mock-jwt-token-" + user.getId() + "-" + user.getRole())
                        .build();
            }
        }
        return LoginResponse.builder()
                .success(false)
                .message("Credenciales inválidas. Verifique su correo o contraseña.")
                .build();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getStudents() {
        return userRepository.findByRole(Role.ESTUDIANTE);
    }

    @Transactional(readOnly = true)
    public StudentProgressDto getStudentProgress(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + studentId));

        if (student.getRole() != Role.ESTUDIANTE) {
            throw new IllegalArgumentException("El usuario con ID " + studentId + " no tiene rol de ESTUDIANTE");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        int totalAttendances = attendanceRepository.findByStudentId(studentId).size();

        double completed = student.getCompletedHours() != null ? student.getCompletedHours() : 0.0;
        double required = student.getRequiredHours() != null ? student.getRequiredHours() : 60.0;
        double pending = Math.max(0.0, required - completed);
        double percentage = required > 0 ? Math.min(100.0, (completed / required) * 100.0) : 0.0;

        return StudentProgressDto.builder()
                .studentId(student.getId())
                .fullName(student.getFullName())
                .institutionalCode(student.getInstitutionalCode())
                .academicProgram(student.getAcademicProgram())
                .completedHours(completed)
                .requiredHours(required)
                .pendingHours(pending)
                .progressPercentage(Math.round(percentage * 10.0) / 10.0)
                .totalEnrollments(enrollments.size())
                .totalAttendances(totalAttendances)
                .build();
    }
}
