package com.shofiqul.scrapper.service;

import com.shofiqul.scrapper.model.PlayStoreIconDto;

public interface ScraperService {
    PlayStoreIconDto getPlayStoreData();

    boolean createFilesFromData();
}
