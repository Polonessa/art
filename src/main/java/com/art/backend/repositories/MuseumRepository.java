package com.art.backend.repositories;

import com.art.backend.models.Museum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuseumRepository extends JpaRepository<Museum, Long> {
}
