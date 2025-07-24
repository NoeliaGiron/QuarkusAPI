package org.acme;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Movie extends PanacheEntity {
    @Column(nullable = false, unique = true)
    public String title;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public Set<Review> reviews = new HashSet<>();
} 