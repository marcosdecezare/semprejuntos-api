package com.sempremjuntos.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class GeoLocationResolverService {

    private final RestTemplate rest = new RestTemplate();

    @Value("${mls.api.url:https://location.services.mozilla.com/v1/geolocate}")
    private String apiUrl;

    @Value("${mls.api.key:test}")
    private String apiKey;

    public static record GeoPoint(double latitude, double longitude, Double accuracyMeters) {}

    public Optional<GeoPoint> resolveLbs(int mcc, int mnc, int lac, int cid, Integer signalStrength) {
        String url = apiUrl + "?key=" + apiKey;

        Map<String, Object> cell = Map.of(
                "mobileCountryCode", mcc,
                "mobileNetworkCode", mnc,
                "locationAreaCode", lac,
                "cellId", cid
        );
        if (signalStrength != null) {
            cell = new java.util.HashMap<>(cell);
            ((java.util.HashMap<String, Object>) cell).put("signalStrength", signalStrength);
        }

        Map<String, Object> body = Map.of("cellTowers", java.util.List.of(cell));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> resp = rest.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);
            Map<String, Object> map = resp.getBody();
            if (map != null && map.containsKey("location")) {
                Map<String, Object> loc = (Map<String, Object>) map.get("location");
                double lat = ((Number) loc.get("lat")).doubleValue();
                double lon = ((Number) loc.get("lng")).doubleValue();
                Double acc = map.containsKey("accuracy") ? ((Number) map.get("accuracy")).doubleValue() : null;
                return Optional.of(new GeoPoint(lat, lon, acc));
            }
        } catch (Exception ignored) {}

        return Optional.empty();
    }
}
