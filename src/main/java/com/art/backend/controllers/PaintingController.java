package com.art.backend.controllers;

import com.art.backend.models.Artist;
import com.art.backend.models.Country;
import com.art.backend.models.Museum;
import com.art.backend.models.Painting;
import com.art.backend.repositories.ArtistRepository;
import com.art.backend.repositories.CountryRepository;
import com.art.backend.repositories.MuseumRepository;
import com.art.backend.repositories.PaintingRepository;
import com.art.backend.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class PaintingController {
    @Autowired
    PaintingRepository paintingRepository;
    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    MuseumRepository museumRepository;

    @GetMapping("/paintings")
    public Page<Painting> getAllPaintings(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return paintingRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }
    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting painting) throws DataValidationException {
        try {
            if (painting.artistid != null) {
                Optional<Artist>
                        art = artistRepository.findById(painting.artistid.id);
                if (art.isEmpty()) {
                    throw new DataValidationException("Неизвестный художник");
                }
                else{
                    art.ifPresent(artist -> painting.artistid = artist);
                }
            } else {
                Optional<Artist> art=null;
                throw new DataValidationException("Неизвестный художник");
            }
            if (painting.museumid != null) {


                Optional<Museum>
                        mus = museumRepository.findById(painting.museumid.id);
                if (mus.isEmpty()) {
                    throw new DataValidationException("Неизвестный музей");
                }
                else{
                    mus.ifPresent(museum -> painting.museumid = museum);
                }
            }

            Painting nc = paintingRepository.save(painting);
            return ResponseEntity.ok(nc);
        }

        catch(Exception ex) {
            throw new DataValidationException("Неизвестная ошибка");
/*
            String error = "undefinederror";
            Map<String, String> map =  new HashMap<>();
            map.put("error", error);
            System.out.println(error);
            return ResponseEntity.ok(map);*/
        }
    }

    @PutMapping("/paintings/{id}")
    public ResponseEntity<Painting> updatePainting(@PathVariable(value = "id") Long paintingId,
                                                   @RequestBody Painting paintingDetails) {
        Painting painting = null;
        Optional<Painting>
                cc = paintingRepository.findById(paintingId);
        if (cc.isPresent()) {

            Optional<Artist>
                    art = artistRepository.findById(cc.get().artistid.id);
            if(art.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such artist in db");
            }
            Optional<Museum>
                    mus = museumRepository.findById(cc.get().museumid.id);
            if(mus.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such museum in db");
            }

            art.ifPresent(artist -> paintingDetails.artistid = artist);
            mus.ifPresent(museum -> paintingDetails.museumid = museum);
            painting= cc.get();
            painting.name = paintingDetails.name;
            painting.artistid = paintingDetails.artistid;
            painting.museumid = paintingDetails.museumid;
            painting.year = paintingDetails.year;
            paintingRepository.save(painting);
            return ResponseEntity.ok(painting);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Painting not found");
        }
    }

    @PostMapping("/deletepaintings")
    public ResponseEntity<HttpStatus> deletePaintings(@RequestBody List<Painting> paintings) {
        List<Long> listOfIds = new ArrayList<>();
        for (Painting painting : paintings) {
            listOfIds.add(painting.id);
        }
        paintingRepository.deleteAllById(listOfIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/paintings/{id}")
    public ResponseEntity<Object> deletePainting(@PathVariable(value = "id") Long PaintingId) {
        Optional<Painting>
                painting = paintingRepository.findById(PaintingId);
        Map<String, Boolean>
                resp = new HashMap<>();
        if (painting.isPresent()) {
            paintingRepository.delete(painting.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }

}
