package com.shofiqul.scrapper.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_info")
@Data
public class PlayStoreApplicationModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String image;
	private String title;
	private String rating;
}
