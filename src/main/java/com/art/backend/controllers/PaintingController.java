package com.art.backend.controllers;

import com.art.backend.models.Artist;
import com.art.backend.models.Country;
import com.art.backend.models.Painting;
import com.art.backend.repositories.CountryRepository;
import com.art.backend.repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class PaintingController {
    @Autowired
    PaintingRepository paintingRepository;

    @GetMapping("/paintings")
    public List
    getAllPaintings() {
        return paintingRepository.findAll();
    }
    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting painting)
            throws Exception {
        try {
            Painting np = paintingRepository.save(painting);
            return new ResponseEntity<Object>(np, HttpStatus.OK);
        } catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("paintings.name_UNIQUE"))
                error = "paintingalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }
    @PutMapping("/paintings/{id}")
    public ResponseEntity<Painting> updatePainting(@PathVariable(value = "id") Long paintingId,
                                                 @RequestBody Painting paintingDetails) {
        Painting painting = null;
        Optional<Painting>
                cc = paintingRepository.findById(paintingId);
        if (cc.isPresent()) {
            painting = cc.get();
            painting.name = paintingDetails.name;
            painting.museumid = paintingDetails.museumid;
            paintingRepository.save(painting);
            return ResponseEntity.ok(painting);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "painting not found");
        }
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
