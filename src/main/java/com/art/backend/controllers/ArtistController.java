package com.art.backend.controllers;

import com.art.backend.models.Artist;
import com.art.backend.models.Country;
import com.art.backend.repositories.ArtistRepository;
import com.art.backend.repositories.CountryRepository;
import com.art.backend.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class ArtistController {
@Autowired
    ArtistRepository artistRepository;
@Autowired
    CountryRepository countryRepository;

    @GetMapping("/artists")
    public Page<Artist> getAllArtists(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return artistRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }
        @PostMapping("/artists")
        public ResponseEntity<Object> createArtist(@RequestBody Artist artist) throws DataValidationException {
        try {
            String ci = artist.country.name;
            Optional<Country> cc = countryRepository.findByParam(ci);
            if (cc.isPresent()) {
                artist.country = cc.get();
            }
            else
                throw new DataValidationException("Неизвестная страна");
        Artist na = artistRepository.save(artist);
        return new ResponseEntity<Object>(na, HttpStatus.OK);
        }catch (Exception ex) {
            if (ex.getMessage().contains("artists.name_UNIQUE"))
                throw new DataValidationException("Этот художник уже есть в базе");
            else
                if (ex.getMessage().contains("Неизвестная страна"))
                     throw new DataValidationException("Неизвестная страна");
                else
                    throw new DataValidationException("Неизвестная ошибка");
        }
    }
@PutMapping("/artists/{id}")
public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long ArtistId,
    @RequestBody Artist artistDetails) throws DataValidationException {
    Artist artist = null;
    try {
        Optional<Artist> cc = artistRepository.findById(ArtistId);
        if (cc.isPresent()) {
            artist = cc.get();
            artist.name = artistDetails.name;
            String ci = artistDetails.country.name;
            Optional<Country> cn = countryRepository.findByParam(ci);
            if (cn.isPresent()) {
                artist.country = cn.get();
            } else
                throw new DataValidationException("Неизвестная страна");
            artist.age = artistDetails.age;
            artistRepository.save(artist);
        } else
            throw new DataValidationException("Неизвестный художник");
    }catch (Exception ex) {
        if (ex.getMessage().contains("Неизвестный художник"))
            throw new DataValidationException("Неизвестный художник");
        else
        if (ex.getMessage().contains("Неизвестная страна"))
            throw new DataValidationException("Неизвестная страна");
        else
            throw new DataValidationException("Неизвестная ошибка");
        }
    return ResponseEntity.ok(artist);
    }
@DeleteMapping("/artists/{id}")
public ResponseEntity<Object> deleteArtist(@PathVariable(value = "id") Long artistId) {
        Optional<Artist>
                artist = artistRepository.findById(artistId);
                        Map<String, Boolean>
                resp = new HashMap<>();
        if (artist.isPresent()) {
            artistRepository.delete(artist.get());
            resp.put("deleted", Boolean.TRUE);
            }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/deleteartists")
    public ResponseEntity deleteArtists(@Validated @RequestBody List<Artist> artists) {
        artistRepository.deleteAll(artists);
        return new ResponseEntity(HttpStatus.OK);
    }

}