package com.shofiqul.scrapper.repo;

import com.shofiqul.scrapper.model.PlayStoreApplicationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayStoreApplicationRepo extends JpaRepository<PlayStoreApplicationModel, Long> {
}
