package com.shofiqul.scrapper.model;

import lombok.Data;

import java.util.Set;

@Data
public class PlayStoreDto {
	private String sectionTitle;
	private Set<ApplicationItems> items;
}
