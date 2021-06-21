package com.neoway.documento.controller;

import com.neoway.documento.business.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusEndpoint {
	@GetMapping("/status")
	public Status getStatus() {
		return new Status();
	}
}
