package com.art.backend.controllers;

import com.art.backend.models.Museum;
import com.art.backend.models.Painting;
import com.art.backend.repositories.MuseumRepository;
import com.art.backend.repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MuseumController {
    @Autowired
    MuseumRepository museumRepository;

    @GetMapping("/museums")
    public List
    getAllMuseums() {
        return museumRepository.findAll();
    }
    @PostMapping("/museums")
    public ResponseEntity<Object> createPainting(@RequestBody Museum museum)
            throws Exception {
        try {
            Museum nm = museumRepository.save(museum);
            return new ResponseEntity<Object>(nm, HttpStatus.OK);
        } catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("museum.name_UNIQUE"))
                error = "museumalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }
    @PutMapping("/museums/{id}")
    public ResponseEntity<Museum> updateMuseum(@PathVariable(value = "id") Long MuseumId,
                                                  @RequestBody Museum museumDetails) {
        Museum museum = null;
        Optional<Museum>
                cc = museumRepository.findById(MuseumId);
        if (cc.isPresent()) {
            museum = cc.get();
            museum.name = museumDetails.name;
            museumRepository.save(museum);
            return ResponseEntity.ok(museum);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "painting not found");
        }
    }
    @DeleteMapping("/museums/{id}")
    public ResponseEntity<Object> deleteMuseum(@PathVariable(value = "id") Long MuseumId) {
        Optional<Museum>
                museum = museumRepository.findById(MuseumId);
        Map<String, Boolean>
                resp = new HashMap<>();
        if (museum.isPresent()) {
            museumRepository.delete(museum.get());
            resp.put("deleted", Boolean.TRUE);
        }
        else
            resp.put("deleted", Boolean.FALSE);
        return ResponseEntity.ok(resp);
    }
}
