package com.shofiqul.scrapper.controller;

import com.shofiqul.scrapper.model.PlayStoreDto;
import com.shofiqul.scrapper.model.ResponseDto;
import com.shofiqul.scrapper.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ScraperController {
	private final ScraperService scraperService;

	@GetMapping("/{model}")
	public Set<ResponseDto> getVehicleByModel(@PathVariable("model") String vehicleModel) {
		return scraperService.getVehicleByModel(vehicleModel);
	}

	@GetMapping("/playstore")
	public PlayStoreDto getPlayStoreData() {
		return scraperService.getPlayStoreData();
	}

}
