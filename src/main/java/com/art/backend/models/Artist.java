package com.art.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
@Access(AccessType.FIELD)
public class Artist {
    public Artist() { }
    public Artist(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    @Column(name = "age", nullable = false)
    public String age;

    @ManyToOne()
    @JoinColumn(name = "countryid")
    public Country country;

    @JsonIgnore
    @OneToMany(mappedBy = "artistid")
    public List<Painting> paintings = new ArrayList<Painting>();

}
