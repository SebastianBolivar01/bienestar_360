package com.ucc.bienestar360.controller;

import com.ucc.bienestar360.dto.QrGenerateResponse;
import com.ucc.bienestar360.dto.ScanQrRequest;
import com.ucc.bienestar360.dto.ScanQrResponse;
import com.ucc.bienestar360.service.AttendanceService;
import com.ucc.bienestar360.service.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QrController {

    private final QrService qrService;
    private final AttendanceService attendanceService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateQr(@RequestParam Long activityId, @RequestParam Long userId) {
        try {
            QrGenerateResponse response = qrService.generateQrForActivity(activityId, userId);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/scan")
    public ResponseEntity<?> scanQr(@RequestBody ScanQrRequest request) {
        try {
            ScanQrResponse response = attendanceService.registerAttendanceViaQr(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
