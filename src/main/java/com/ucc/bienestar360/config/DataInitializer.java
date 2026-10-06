package com.ucc.bienestar360.config;

import com.ucc.bienestar360.model.*;
import com.ucc.bienestar360.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final QrSessionRepository qrSessionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        // 1. Seed Users
        User student1 = userRepository.save(User.builder()
                .email("estudiante.pasto@ucc.edu.co")
                .password("123456")
                .fullName("Carlos Andrés Coral")
                .institutionalCode("748291")
                .academicProgram("Ingeniería de Software")
                .campus("Pasto")
                .role(Role.ESTUDIANTE)
                .completedHours(28.0)
                .requiredHours(60.0)
                .build());

        User student2 = userRepository.save(User.builder()
                .email("estudiante2.pasto@ucc.edu.co")
                .password("123456")
                .fullName("María Fernanda Vallejo")
                .institutionalCode("748292")
                .academicProgram("Medicina")
                .campus("Pasto")
                .role(Role.ESTUDIANTE)
                .completedHours(42.0)
                .requiredHours(60.0)
                .build());

        User student3 = userRepository.save(User.builder()
                .email("estudiante3.pasto@ucc.edu.co")
                .password("123456")
                .fullName("Juan David Narváez")
                .institutionalCode("748293")
                .academicProgram("Psicología")
                .campus("Pasto")
                .role(Role.ESTUDIANTE)
                .completedHours(12.0)
                .requiredHours(60.0)
                .build());

        User bienestarStaff = userRepository.save(User.builder()
                .email("bienestar.docente@ucc.edu.co")
                .password("123456")
                .fullName("Dra. Elena Benavides")
                .institutionalCode("DOC-901")
                .academicProgram("Bienestar Universitario")
                .campus("Pasto")
                .role(Role.DOCENTE_BIENESTAR)
                .completedHours(0.0)
                .requiredHours(0.0)
                .build());

        User director = userRepository.save(User.builder()
                .email("director.seccional@ucc.edu.co")
                .password("123456")
                .fullName("Ing. Roberto Rosero")
                .institutionalCode("DIR-001")
                .academicProgram("Dirección Seccional")
                .campus("Pasto")
                .role(Role.DIRECTOR)
                .completedHours(0.0)
                .requiredHours(0.0)
                .build());

        User regularProf = userRepository.save(User.builder()
                .email("docente.regular@ucc.edu.co")
                .password("123456")
                .fullName("Prof. Fernando Portilla")
                .institutionalCode("DOC-102")
                .academicProgram("Ingeniería de Software")
                .campus("Pasto")
                .role(Role.DOCENTE_REGULAR)
                .completedHours(0.0)
                .requiredHours(0.0)
                .build());

        User admin = userRepository.save(User.builder()
                .email("admin.sistema@ucc.edu.co")
                .password("123456")
                .fullName("Administrador UCC Pasto")
                .institutionalCode("ADM-001")
                .academicProgram("Tecnologías de Información")
                .campus("Pasto")
                .role(Role.ADMIN_INSTITUCIONAL)
                .completedHours(0.0)
                .requiredHours(0.0)
                .build());

        // 2. Seed Activities
        Activity act1 = activityRepository.save(Activity.builder()
                .title("Torneo Interfacultades de Fútbol Sala UCC 2026")
                .description("Evento deportivo de integración universitaria. Otorga 8 horas de bienestar a participantes activos.")
                .category("Deporte")
                .location("Polideportivo Sede Pasto")
                .startTime(LocalDateTime.now().plusDays(2).withHour(14).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(2).withHour(18).withMinute(0))
                .hoursGranted(8.0)
                .totalSlots(40)
                .availableSlots(38)
                .status(ActivityStatus.PROGRAMADA)
                .createdBy(bienestarStaff)
                .build());

        Activity act2 = activityRepository.save(Activity.builder()
                .title("Taller de Manejo del Estrés y Salud Mental")
                .description("Taller práctico dictado por la unidad de desarrollo humano para fortalecer la salud mental estudiantil.")
                .category("Salud & Desarrollo")
                .location("Auditorio Central - Bloque B")
                .startTime(LocalDateTime.now().withHour(9).withMinute(0))
                .endTime(LocalDateTime.now().withHour(13).withMinute(0))
                .hoursGranted(4.0)
                .totalSlots(30)
                .availableSlots(27)
                .status(ActivityStatus.EN_CURSO)
                .createdBy(bienestarStaff)
                .build());

        Activity act3 = activityRepository.save(Activity.builder()
                .title("Jornada de Donación de Sangre y Salud Preventiva")
                .description("Campañana institucional de solidaridad y hábitos saludables en alianza con el Banco de Sangre de Nariño.")
                .category("Salud")
                .location("Plazoleta Principal Sede Pasto")
                .startTime(LocalDateTime.now().plusDays(5).withHour(8).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(5).withHour(12).withMinute(0))
                .hoursGranted(6.0)
                .totalSlots(50)
                .availableSlots(49)
                .status(ActivityStatus.PROGRAMADA)
                .createdBy(bienestarStaff)
                .build());

        Activity act4 = activityRepository.save(Activity.builder()
                .title("Festival Cultural de Danza e Integración Universitaria")
                .description("Muestra cultural de grupos representativos de danza folclórica y moderna de la UCC.")
                .category("Cultura")
                .location("Teatro Imperial / Aula Magna")
                .startTime(LocalDateTime.now().minusDays(3))
                .endTime(LocalDateTime.now().minusDays(3).plusHours(5))
                .hoursGranted(5.0)
                .totalSlots(100)
                .availableSlots(97)
                .status(ActivityStatus.FINALIZADA)
                .createdBy(bienestarStaff)
                .build());

        // 3. Seed Enrollments
        enrollmentRepository.save(Enrollment.builder()
                .student(student1)
                .activity(act1)
                .enrollmentDate(LocalDateTime.now().minusDays(1))
                .status(EnrollmentStatus.INSCRITO)
                .build());

        enrollmentRepository.save(Enrollment.builder()
                .student(student1)
                .activity(act2)
                .enrollmentDate(LocalDateTime.now().minusHours(3))
                .status(EnrollmentStatus.INSCRITO)
                .build());

        enrollmentRepository.save(Enrollment.builder()
                .student(student2)
                .activity(act2)
                .enrollmentDate(LocalDateTime.now().minusHours(2))
                .status(EnrollmentStatus.INSCRITO)
                .build());

        enrollmentRepository.save(Enrollment.builder()
                .student(student1)
                .activity(act4)
                .enrollmentDate(LocalDateTime.now().minusDays(4))
                .status(EnrollmentStatus.ASISTIO)
                .build());

        // 4. Seed Past Attendance for History
        attendanceRepository.save(Attendance.builder()
                .student(student1)
                .activity(act4)
                .timestamp(LocalDateTime.now().minusDays(3))
                .status("VERIFICADA")
                .hoursAwarded(5.0)
                .build());

        // 5. Seed active QR session for act2 (Taller de Manejo del Estrés)
        qrSessionRepository.save(QrSession.builder()
                .activity(act2)
                .token("UCC-QR-2-DEMO-PASTO")
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .expiresAt(LocalDateTime.now().plusHours(3))
                .generatedBy(bienestarStaff)
                .active(true)
                .build());
    }
}
