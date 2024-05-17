package com.art.backend.controllers;

import com.art.backend.models.Country;
import com.art.backend.models.Museum;
import com.art.backend.models.Painting;
import com.art.backend.repositories.MuseumRepository;
import com.art.backend.repositories.PaintingRepository;
import com.art.backend.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.thymeleaf.util.StringUtils.length;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class MuseumController {
    @Autowired
    MuseumRepository museumRepository;

    @Autowired
    PaintingRepository paintingRepository;
    @GetMapping("/museums")
    public Page<Museum> getAllMuseums(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return museumRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }
    @PostMapping("/museums")
    public ResponseEntity<Object> createPainting(@RequestBody Museum museum)
            throws DataValidationException {
        try {
            Museum nm = museumRepository.save(museum);
            return new ResponseEntity<Object>(nm, HttpStatus.OK);
        } catch (Exception ex) {
            if (ex.getMessage().contains("museum.name_UNIQUE"))
                throw new DataValidationException("Этот музей уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }
    @PutMapping("/museums/{id}")
    public ResponseEntity<Museum> updateMuseum(@PathVariable(value = "id") Long MuseumId,
                                                  @RequestBody Museum museumDetails) throws DataValidationException {
        try {
            Museum museum = museumRepository.findById(MuseumId)
                    .orElseThrow(() -> new DataValidationException("музей с таким индексом не найден"));
            museum.name = museumDetails.name;
            museum.location = museumDetails.location;
            museumRepository.save(museum);
            return ResponseEntity.ok(museum);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("museums.name_UNIQUE"))
                throw new DataValidationException("Этот музей уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
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

    @PostMapping("/deletemuseums")
    public ResponseEntity deleteMuseums(@Validated @RequestBody List<Museum> museums) {
       /* for (int i = 0; i < museums.size(); i++)
            paintingRepository.deleteAll(museums.get(i).paintings);*/
        museumRepository.deleteAll(museums);
        return new ResponseEntity(HttpStatus.OK);
    }
}
