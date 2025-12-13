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

    @Value("${app.uploads.base-path:/opt/semprejunttos/uploads}")
    private String uploadsBasePath;

    // Agora o default já aponta para o domínio HTTPS
    @Value("${app.uploads.base-url:https://api.semprejunttos.com.br}")
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

        // Construir URL pública (normalizando barra final)
        String base = normalizeBaseUrl(uploadsBaseUrl);
        return base + "/patients/" + fileName;
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

    /**
     * Remove barra no final se o base-url vier com "/"
     * para evitar "//patients/..."
     */
    private String normalizeBaseUrl(String url) {
        if (url == null) {
            return "";
        }
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}
