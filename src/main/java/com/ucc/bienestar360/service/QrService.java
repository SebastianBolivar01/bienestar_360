package com.ucc.bienestar360.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ucc.bienestar360.dto.QrGenerateResponse;
import com.ucc.bienestar360.model.*;
import com.ucc.bienestar360.repository.ActivityRepository;
import com.ucc.bienestar360.repository.QrSessionRepository;
import com.ucc.bienestar360.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrSessionRepository qrSessionRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    @Transactional
    public QrGenerateResponse generateQrForActivity(Long activityId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getRole() != Role.DOCENTE_BIENESTAR && user.getRole() != Role.ADMIN_INSTITUCIONAL) {
            throw new SecurityException("Acceso Denegado: Los profesores regulares o estudiantes no tienen permisos para generar códigos QR.");
        }

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada con ID: " + activityId));

        qrSessionRepository.findByActivityId(activityId).forEach(s -> {
            s.setActive(false);
            qrSessionRepository.save(s);
        });

        if (activity.getStatus() == ActivityStatus.PROGRAMADA) {
            activity.setStatus(ActivityStatus.EN_CURSO);
            activityRepository.save(activity);
        }

        String token = "UCC-QR-" + activityId + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusHours(4);

        QrSession session = QrSession.builder()
                .activity(activity)
                .token(token)
                .createdAt(now)
                .expiresAt(expiresAt)
                .generatedBy(user)
                .active(true)
                .build();

        session = qrSessionRepository.save(session);

        String base64QrImage = generateQrBase64(token, 300, 300);

        return QrGenerateResponse.builder()
                .qrSessionId(session.getId())
                .activityId(activity.getId())
                .activityTitle(activity.getTitle())
                .token(token)
                .expiresAt(expiresAt)
                .qrBase64Image(base64QrImage)
                .build();
    }

    public String generateQrBase64(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] pngData = outputStream.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el código QR visual: " + e.getMessage(), e);
        }
    }
}
