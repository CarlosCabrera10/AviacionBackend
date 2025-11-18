package com.example.AviacionBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String API_KEY = "66088af6a39b306684aa2ce9bc16ed73";

    @GetMapping("/current")
    public ResponseEntity<String> getWeather(
            @RequestParam double lat,
            @RequestParam double lon
    ) {

        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + lat
                + "&lon=" + lon
                + "&units=metric"
                + "&lang=es"
                + "&appid=" + API_KEY;

        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }
}
