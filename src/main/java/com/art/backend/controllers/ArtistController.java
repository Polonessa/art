package com.art.backend.controllers;

import com.art.backend.models.Artist;
import com.art.backend.models.Country;
import com.art.backend.repositories.ArtistRepository;
import com.art.backend.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class ArtistController {
@Autowired
    ArtistRepository artistRepository;
@Autowired
    CountryRepository countryRepository;

@GetMapping("/artists")
public List
        getAllArtists() {
        return artistRepository.findAll();
        }
@PostMapping("/artists")
public ResponseEntity<Object> createArtist(@RequestBody Artist artist) throws Exception {
        try {
          Long ci = artist.country.id;
            Optional<Country>
                    cc = countryRepository.findById(ci);
            if (cc.isPresent()) {
                artist.country = cc.get();
            }
        Artist na = artistRepository.save(artist);
        return new ResponseEntity<Object>(na, HttpStatus.OK);
        }catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("artists.name_UNIQUE"))
                error = "countryalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }
@PutMapping("/artists/{id}")
public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long ArtistId,
@RequestBody Artist artistDetails) {
        Artist artist = null;
        Optional<Artist>
                cc = artistRepository.findById(ArtistId);
                        if (cc.isPresent()) {
                            artist = cc.get();
                            artist.name = artistDetails.name;
                            artistRepository.save(artist);
                            return ResponseEntity.ok(artist);
                            }
                        else {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
                        }
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

}