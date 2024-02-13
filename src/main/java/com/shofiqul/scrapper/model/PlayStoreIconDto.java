package com.shofiqul.scrapper.model;

import lombok.Data;

import java.util.Set;

@Data
public class PlayStoreIconDto {
	private String sectionTitle;
	private Set<IconInformation> items;
}
