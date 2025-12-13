package com.sempremjuntos.api.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Serviço responsável por resolver localização (latitude/longitude)
 * a partir de dados de torres (LBS) ou redes Wi-Fi, usando a Google Geolocation API.
 *
 * - Prioriza Google (preciso, cobertura mundial)
 * - Mantém compatibilidade com Mozilla (opcional, como fallback)
 */
@Service
public class GeoLocationResolverService {

    @Value("${google.api.geolocation.key}")
    private String googleApiKey;

    private static final String GOOGLE_URL =
            "https://www.googleapis.com/geolocation/v1/geolocate?key=%s";

    private static final String MOZILLA_URL =
            "https://location.services.mozilla.com/v1/geolocate?key=test";

    private final RestTemplate rest = new RestTemplate();

    /**
     * Resolve localização via Google API.
     */
    public Optional<ResolvedLocation> resolveLbs(Integer mcc, Integer mnc, Integer lac, Integer cid, Integer signalStrength) {
        try {
            // Monta JSON de requisição
            JSONObject payload = new JSONObject();
            var tower = new JSONObject();
            tower.put("mobileCountryCode", mcc);
            tower.put("mobileNetworkCode", mnc);
            tower.put("locationAreaCode", lac);
            tower.put("cellId", cid);
            if (signalStrength != null) {
                tower.put("signalStrength", signalStrength);
            }
            payload.put("cellTowers", new org.json.JSONArray().put(tower));

            // Cabeçalhos HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(payload.toString(), headers);

            // 🔹 1️⃣ Primeira tentativa: Google Geolocation API
            String url = String.format(GOOGLE_URL, googleApiKey);
            ResponseEntity<String> response = rest.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject body = new JSONObject(response.getBody());
                if (body.has("location")) {
                    JSONObject loc = body.getJSONObject("location");
                    double lat = loc.getDouble("lat");
                    double lon = loc.getDouble("lng");
                    double accuracy = body.optDouble("accuracy", 0);
                    System.out.printf("[LBS-GOOGLE] Resolved: lat=%.7f lon=%.7f acc=%.1fm%n", lat, lon, accuracy);
                    return Optional.of(new ResolvedLocation(lat, lon, accuracy));
                }
            }

            // 🔹 2️⃣ Fallback: Mozilla API (opcional)
            System.out.printf("[LBS-GOOGLE] Google não retornou dados, tentando Mozilla...%n");
            response = rest.exchange(MOZILLA_URL, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject body = new JSONObject(response.getBody());
                if (body.has("location")) {
                    JSONObject loc = body.getJSONObject("location");
                    double lat = loc.getDouble("lat");
                    double lon = loc.getDouble("lng");
                    double accuracy = body.optDouble("accuracy", 0);
                    System.out.printf("[LBS-MOZILLA] Resolved: lat=%.7f lon=%.7f acc=%.1fm%n", lat, lon, accuracy);
                    return Optional.of(new ResolvedLocation(lat, lon, accuracy));
                }
            }

            System.out.printf("[LBS] Nenhum provedor retornou coordenadas válidas.%n");
            return Optional.empty();

        } catch (Exception e) {
            System.err.printf("[LBS-ERROR] Erro ao resolver LBS: %s%n", e.getMessage());
            return Optional.empty();
        }
    }


    /**
     * Resolve localização combinando LBS (torres) e Wi-Fi (pontos de acesso).
     */
    public Optional<ResolvedLocation> resolveLbsWithWifi(
            Integer mcc,
            Integer mnc,
            Integer lac,
            Integer cid,
            Integer signalStrength,
            java.util.List<WifiAccessPoint> wifiPoints) {

        try {
            // Monta JSON de requisição
            JSONObject payload = new JSONObject();

            // Torres de celular (LBS), se estiverem disponíveis
            if (mcc != null && mnc != null && lac != null && cid != null) {
                var tower = new JSONObject();
                tower.put("mobileCountryCode", mcc);
                tower.put("mobileNetworkCode", mnc);
                tower.put("locationAreaCode", lac);
                tower.put("cellId", cid);
                if (signalStrength != null) {
                    tower.put("signalStrength", signalStrength);
                }
                payload.put("cellTowers", new org.json.JSONArray().put(tower));
            }

            // Pontos de acesso Wi-Fi
            if (wifiPoints != null && !wifiPoints.isEmpty()) {
                var wifiArray = new org.json.JSONArray();
                for (WifiAccessPoint ap : wifiPoints) {
                    if (ap.macAddress() == null || ap.macAddress().isBlank()) continue;
                    var w = new JSONObject();
                    w.put("macAddress", ap.macAddress());
                    if (ap.signalStrength() != null) {
                        w.put("signalStrength", ap.signalStrength());
                    }
                    wifiArray.put(w);
                }
                if (wifiArray.length() > 0) {
                    payload.put("wifiAccessPoints", wifiArray);
                }
            }

            // Cabeçalhos HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(payload.toString(), headers);

            // 1️⃣ Google Geolocation API
            String url = String.format(GOOGLE_URL, googleApiKey);
            ResponseEntity<String> response = rest.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject body = new JSONObject(response.getBody());
                if (body.has("location")) {
                    JSONObject loc = body.getJSONObject("location");
                    double lat = loc.getDouble("lat");
                    double lon = loc.getDouble("lng");
                    double accuracy = body.optDouble("accuracy", 0);
                    System.out.printf("[LBS+WIFI-GOOGLE] Resolved: lat=%.7f lon=%.7f acc=%.1fm%n",
                            lat, lon, accuracy);
                    return Optional.of(new ResolvedLocation(lat, lon, accuracy));
                }
            }

            // 2️⃣ Fallback Mozilla (opcional, se quiser manter)
            System.out.printf("[LBS+WIFI-GOOGLE] Google não retornou dados, tentando Mozilla...%n");
            response = rest.exchange(MOZILLA_URL, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject body = new JSONObject(response.getBody());
                if (body.has("location")) {
                    JSONObject loc = body.getJSONObject("location");
                    double lat = loc.getDouble("lat");
                    double lon = loc.getDouble("lng");
                    double accuracy = body.optDouble("accuracy", 0);
                    System.out.printf("[LBS+WIFI-MOZILLA] Resolved: lat=%.7f lon=%.7f acc=%.1fm%n",
                            lat, lon, accuracy);
                    return Optional.of(new ResolvedLocation(lat, lon, accuracy));
                }
            }

            System.out.printf("[LBS+WIFI] Nenhum provedor retornou coordenadas válidas.%n");
            return Optional.empty();

        } catch (Exception e) {
            System.err.printf("[LBS+WIFI-ERROR] Erro ao resolver localização combinada: %s%n", e.getMessage());
            return Optional.empty();
        }
    }




    /**
     * DTO simples para resposta resolvida.
     */
    public record ResolvedLocation(double latitude, double longitude, double accuracyMeters) {}
    /**
     * Representa um ponto de acesso Wi-Fi para a Google Geolocation API.
     */
    public record WifiAccessPoint(String macAddress, Integer signalStrength) {}

}
