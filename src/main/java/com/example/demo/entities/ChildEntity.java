package com.example.demo.entities;

import javax.persistence.*;

@javax.persistence.Entity
public class ChildEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    String name;

    @ManyToOne(optional = false)
    public Entity parent;

}
