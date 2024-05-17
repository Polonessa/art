package com.art.backend.repositories;

import com.art.backend.models.Artist;
import com.art.backend.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @Query("SELECT r FROM Artist r WHERE r.name= ?1")
    Optional<Country> findByParam(String param);
}
