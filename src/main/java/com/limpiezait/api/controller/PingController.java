package com.limpiezait.api.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	@GetMapping("/api/ping")
	public Map<String, String> ping() {
		return Map.of("estado", "ok", "app", "LimpiezaIT");
	}

}
