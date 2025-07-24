package org.acme;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Review extends PanacheEntity {
    @ManyToOne(optional = false)
    public Movie movie;

    @ManyToOne(optional = false)
    public Critic critic;

    @Column(nullable = false)
    public int rating;

    public String comment;
} 