package com.sempremjuntos.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class PatientPhotoStorageService {

    @Value("${app.uploads.base-path:/opt/semprejuntos/uploads}")
    private String uploadsBasePath;

    @Value("${app.uploads.base-url:http://163.176.178.73:8080}")
    private String uploadsBaseUrl;

    /**
     * Salva a foto do device no filesystem e retorna a URL pública.
     */
    public String saveDevicePhoto(Integer deviceId, MultipartFile file) throws IOException {

        // Criar /patients se não existir
        Path patientDir = Paths.get(uploadsBasePath, "patients");
        if (!Files.exists(patientDir)) {
            Files.createDirectories(patientDir);
        }

        // Detectar extensão real do arquivo
        String original = file.getOriginalFilename();
        String ext = extractExtension(original);

        // Nome final do arquivo
        String fileName = "device_" + deviceId + "_" + System.currentTimeMillis() + ext;

        Path target = patientDir.resolve(fileName);

        // Salvar arquivo no disco
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // Construir URL pública
        return uploadsBaseUrl + "/patients/" + fileName;
    }

    private String extractExtension(String originalName) {
        if (originalName == null) {
            return ".jpg"; // fallback
        }

        String lower = originalName.toLowerCase();

        if (lower.endsWith(".jpeg")) return ".jpeg";
        if (lower.endsWith(".jpg")) return ".jpg";
        if (lower.endsWith(".png")) return ".png";
        if (lower.endsWith(".webp")) return ".webp";

        // fallback caso seja algo inesperado
        int dot = lower.lastIndexOf(".");
        return dot >= 0 ? lower.substring(dot) : ".jpg";
    }
}
