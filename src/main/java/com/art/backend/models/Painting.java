package com.art.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "paitings")
@Access(AccessType.FIELD)
public class Painting {
    public Painting() { }
    public Painting(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;
    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "year")
    public String year;

    @ManyToOne()
    @JoinColumn(name = "artistid")
    public Artist artistid;

    @ManyToOne()
    @JoinColumn(name = "museumid")
    public Museum museumid;
}
