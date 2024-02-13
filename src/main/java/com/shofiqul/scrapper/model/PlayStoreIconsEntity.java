package com.shofiqul.scrapper.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "playstore_icons")
@Data
public class PlayStoreIconsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String imageUrl;
	private String title;
	private String rating;
}
