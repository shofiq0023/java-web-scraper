package com.shofiqul.scrapper.service;

import com.shofiqul.scrapper.model.PlayStoreDto;
import com.shofiqul.scrapper.model.ResponseDto;

import java.util.Set;

public interface ScraperService {
    public Set<ResponseDto> getVehicleByModel(String vehicalModel);

    PlayStoreDto getPlayStoreData();
}
