package com.example.demo.entities;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
public class Entity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    public String name = "";

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.EAGER)
    Set<ChildEntity> children;
}
