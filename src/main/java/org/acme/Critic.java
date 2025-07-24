package org.acme;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Critic extends PanacheEntity {
    // El campo id ya lo hereda de PanacheEntity

    @Column(nullable = false, unique = true)
    public String name;
} 