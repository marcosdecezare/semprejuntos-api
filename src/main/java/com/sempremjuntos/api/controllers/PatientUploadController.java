package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.services.PatientPhotoStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = "*")
public class PatientUploadController {

    private final PatientPhotoStorageService storageService;

    public PatientUploadController(PatientPhotoStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/device-photo")
    @Operation(
            summary = "Upload de foto vinculada ao device",
            description = "Recebe o arquivo via multipart e devolve a URL pública.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<UploadResponse> uploadDevicePhoto(
            @RequestParam("deviceId") Integer deviceId,
            @RequestPart("file") MultipartFile file
    ) {
        try {
            String url = storageService.saveDevicePhoto(deviceId, file);
            return ResponseEntity.ok(new UploadResponse(url));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // DTO simples para retorno JSON
    public static class UploadResponse {
        private String photoUrl;

        public UploadResponse(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }
}
