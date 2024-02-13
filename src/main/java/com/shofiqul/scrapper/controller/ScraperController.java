package com.shofiqul.scrapper.controller;

import com.shofiqul.scrapper.model.PlayStoreIconDto;
import com.shofiqul.scrapper.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ScraperController {
	private final ScraperService scraperService;

	@GetMapping("/playstore/only/first_section")
	public PlayStoreIconDto getPlayStoreData() {
		return scraperService.getPlayStoreData();
	}

	@PostMapping("/create/files")
	public boolean createFilesFromData() {
		return scraperService.createFilesFromData();
	}
}
